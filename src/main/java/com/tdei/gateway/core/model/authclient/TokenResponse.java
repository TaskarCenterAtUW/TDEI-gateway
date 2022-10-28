package com.tdei.gateway.core.model.authclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResponse {
    @JsonProperty("access_token")
    private String token;
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("refresh_expires_in")
    private long refreshExpiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
