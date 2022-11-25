package com.tdei.gateway.core.service.auth.contract;

import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.core.model.authclient.UserProfile;
import feign.Feign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

import java.util.List;

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
    UserProfile validateAccessToken(String token);

    @RequestLine("POST /api/v1/validateApiKey")
    @Headers({"Content-Type: text/plain"})
    UserProfile validateApiKey(String apiKey);

    @RequestLine("GET /api/v1/hasPermission?userId={userId}&agencyId={tdeiOrgId}&affirmative={affirmative}&roles={roles}")
    Boolean hasPermission(@Param("userId") String userId, @Param("tdeiOrgId") String tdeiOrgId, @Param("roles") List<String> roles, @Param("affirmative") Boolean affirmative);
}
