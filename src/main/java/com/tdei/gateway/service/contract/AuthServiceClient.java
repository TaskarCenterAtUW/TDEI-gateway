package com.tdei.gateway.service.contract;

import com.tdei.gateway.model.authclient.LoginModel;
import com.tdei.gateway.model.authclient.TokenResponse;
import feign.Feign;
import feign.Headers;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

@Headers("Accept: application/json")
public interface AuthServiceClient {
    static AuthServiceClient connect(String baseUri) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logLevel(feign.Logger.Level.FULL)
                .logger(new Slf4jLogger(AuthServiceClient.class))
                .target(AuthServiceClient.class, baseUri);
    }

    @RequestLine("POST /api/v1/authenticate")
    @Headers({"Content-Type: application/json"})
    TokenResponse authenticate(LoginModel loginModel);

    @RequestLine("POST /api/v1/validateAccessToken")
    @Headers({"Content-Type: text/plain"})
    TokenResponse validateAccessToken(String token);

    @RequestLine("POST /api/v1/validateApiKey")
    @Headers({"Content-Type: text/plain"})
    TokenResponse validateApiKey(String apiKey);
}
