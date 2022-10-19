package com.tdei.gateway.service.contract;

import com.tdei.gateway.model.authclient.LoginModel;
import com.tdei.gateway.model.authclient.TokenResponse;

import java.security.InvalidKeyException;

public interface IAuthService {
    TokenResponse authenticate(LoginModel loginModel);

    TokenResponse validateAccessToken(String token);

    TokenResponse validateApiKey(String apiKey) throws InvalidKeyException;
}
