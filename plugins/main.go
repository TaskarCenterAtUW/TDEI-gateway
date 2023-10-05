// SPDX-License-Identifier: Apache-2.0
package main

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"tdei-plugin/common"
	tdeitypes "tdei-plugin/types"
	"tdei-plugin/utility"
	"time"
)

// pluginName is the plugin name
var pluginName = "tdei-api-gateway"

// HandlerRegisterer is the symbol the plugin loader will try to load. It must implement the Registerer interface
var HandlerRegisterer = registerer(pluginName)

type registerer string

func (r registerer) RegisterHandlers(f func(
	name string,
	handler func(context.Context, map[string]interface{}, http.Handler) (http.Handler, error),
)) {
	f(string(r), r.registerHandlers)
}

func (registerer) RegisterLogger(v interface{}) {
	l, ok := v.(common.Logger)
	if !ok {
		return
	}
	common.TDEILogger = l
	common.TDEILogger.Debug(fmt.Sprintf("[PLUGIN: %s] Logger loaded", HandlerRegisterer))
}

func (r registerer) registerHandlers(_ context.Context, extra map[string]interface{}, h http.Handler) (http.Handler, error) {
	// //The config variable contains all the keys you have defined in the configuration
	// if the key doesn't exists or is not a map the plugin returns an error and the default handler
	config, ok := extra[pluginName].(map[string]interface{})
	if !ok {
		return h, errors.New("configuration not found")
	}

	pluginConfig := utility.ReadPluginConfig(config)

	common.TDEILogger.Debug(fmt.Sprintf("TDEI plugin is now configured with HTTP middleware %s", pluginName))

	// return the actual handler wrapping or your custom logic so it can be used as a replacement for the default http handler
	return http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {

		authorizationToken := req.Header.Get("Authorization")
		apiKey := req.Header.Get(pluginConfig.ApiKeyHeader)

		common.TDEILogger.Debug("Entered HTTP handler")
		fmt.Println("Entered HTTP handler")

		//api documentation redirect
		if strings.Contains(req.URL.Path, common.ApiDocsURL) {
			http.Redirect(w, req, pluginConfig.ApiDcoumentationUrl, http.StatusPermanentRedirect)
			return
		}

		//Pass through url check
		if utility.PathContains(req.URL.Path, pluginConfig.PassThroughUrls) {
			h.ServeHTTP(w, req)
			return
		}

		if len(authorizationToken) != 0 {
			accessToken, err := utility.ExtractBearerToken(authorizationToken)
			if err != nil {
				message := "Invalid access token format"
				logError(w, message, http.StatusUnauthorized, err)
				return
			}

			bodyReader := bytes.NewBufferString(accessToken)

			requestURL := fmt.Sprintf("%s%s", pluginConfig.AuthServer, common.ValidateAccessTokenURL)
			newReq, err := http.NewRequest(http.MethodPost, requestURL, bodyReader)
			newReq.Header.Set("Content-Type", "text/plain")
			if err != nil {
				message := "Error creating the validateAccessToken request with auth service"
				logError(w, message, http.StatusInternalServerError, err)
				return
			}

			client := http.Client{
				Timeout: 30 * time.Second,
			}

			res, err := client.Do(newReq)
			if err != nil {
				message := "Error validating the access token request with auth service"
				logError(w, message, http.StatusInternalServerError, err)
				return
			} else if res.StatusCode != http.StatusOK {
				common.TDEILogger.Error("Unauthorized request", res)
				fmt.Println("Unauthorized request", res)
				http.Error(w, "Unauthorized request", http.StatusUnauthorized)
				return
			}

			executedApi, error := processGatewayAPIRequests(pluginConfig, w, req)
			if error != nil {
				message := "Error creating the api key request"
				logError(w, message, http.StatusInternalServerError, err)
				return
			} else if !executedApi {
				h.ServeHTTP(w, req)
			}
			return
		} else if len(apiKey) != 0 {
			bodyReader := bytes.NewBufferString(apiKey)

			requestURL := fmt.Sprintf("%s%s", pluginConfig.AuthServer, common.ValidateApiKeyURL)
			newReq, err := http.NewRequest(http.MethodPost, requestURL, bodyReader)
			newReq.Header.Set("Content-Type", "text/plain")
			if err != nil {
				message := "Error creating the api key request with auth service"
				logError(w, message, http.StatusInternalServerError, err)
				return
			}

			client := http.Client{
				Timeout: 30 * time.Second,
			}

			res, err := client.Do(newReq)
			if err != nil {
				message := "Error validating the api key request with auth service"
				logError(w, message, http.StatusInternalServerError, err)
				return
			} else if res.StatusCode != http.StatusOK {
				message := "Unauthorized request"
				logError(w, message, http.StatusUnauthorized, errors.New("Status :"+strconv.Itoa(res.StatusCode)))
				return
			}

			executedApi, error := processGatewayAPIRequests(pluginConfig, w, req)
			if error != nil {
				message := "Error processing the api request"
				logError(w, message, http.StatusInternalServerError, err)
				return
			} else if !executedApi {
				h.ServeHTTP(w, req)
			}
			return

		} else {
			message := "[Unauthorized Access] API key / Access token not provided"
			logError(nil, message, http.StatusUnauthorized, errors.New(message))

			w.Header().Set("Content-Type", "text/html; charset=utf-8")
			w.WriteHeader(http.StatusUnauthorized)
			htmlContent := strings.Replace(common.HelpHTML, "apidoc", pluginConfig.ApiDcoumentationUrl, 1)
			fmt.Fprint(w, htmlContent)
			return
		}

	}), nil
}

func logError(w http.ResponseWriter, message string, status int, err error) {
	common.TDEILogger.Error(message, err)
	fmt.Println(message, err)
	if w != nil {
		http.Error(w, message, status)
	}
}

func processGatewayAPIRequests(pluginConfig tdeitypes.PluginConfig, w http.ResponseWriter, req *http.Request) (bool, error) {

	// Serve /api from Gateway as it is static implementation now
	if strings.Contains(req.URL.Path, common.TdeiAPIEndpoint) {
		w.Header().Set("Content-Type", "application/json")

		version := tdeitypes.Version{
			Version:       "1.0",
			Documentation: pluginConfig.ApiDcoumentationUrl,
			Specification: pluginConfig.ApiSpecificationUrl,
		}

		versions := tdeitypes.Versions{
			Versions: []tdeitypes.Version{version},
		}

		// Convert the Versions object to JSON
		jsonData, err := json.Marshal(versions)
		if err != nil {
			fmt.Println("Error:", err)
			return false, err
		}

		// Set the HTTP status code to 200
		w.WriteHeader(http.StatusOK)
		w.Write(jsonData)
		return true, nil
	}
	return false, nil
}

func main() {}
