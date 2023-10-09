package utility

import (
	"strings"
	tdeitypes "tdei-plugin/types"
	"errors"
)

func PathContains(a string, list []string) bool {
	for _, subStringUrl := range list {
		if strings.Contains(a, subStringUrl) {
			return true
		}
	}
	return false
}

// bearerToken extracts the content from the header, striping the Bearer prefix
func ExtractBearerToken(rawToken string) (string, error) {
	pieces := strings.SplitN(rawToken, " ", 2)

	if len(pieces) < 2 {
		return "", errors.New("token with incorrect bearer format")
	}

	token := strings.TrimSpace(pieces[1])

	return token, nil
}

func ReadPluginConfig(data map[string]interface{}) tdeitypes.PluginConfig {
	apiKeyHeader, _ := data["api_key_header"].(string)
	authServer, _ := data["auth_server"].(string)
	passThroughUrlsConfig, _ := data["pass-through-urls"].(string)
	passThroughUrls := strings.Split(passThroughUrlsConfig, ",")
	apiDcoumentationUrl, _ := data["tdei-api-documentation-url"].(string)
	apiSpecificationUrl, _ := data["tdei-api-specification-url"].(string)

	pluginConfig := tdeitypes.PluginConfig{
		ApiKeyHeader:        apiKeyHeader,
		AuthServer:          authServer,
		PassThroughUrls:     passThroughUrls,
		ApiDcoumentationUrl: apiDcoumentationUrl,
		ApiSpecificationUrl: apiSpecificationUrl,
	}

	return pluginConfig
}
