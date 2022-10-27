package com.tdei.gateway.service.contract;

import com.tdei.gateway.model.authclient.LoginModel;
import com.tdei.gateway.model.authclient.TokenResponse;
import com.tdei.gateway.model.authclient.UserProfile;

import java.security.InvalidKeyException;
import java.security.Principal;

public interface IAuthService {
    TokenResponse authenticate(LoginModel loginModel);

    UserProfile validateAccessToken(String token);

    UserProfile validateApiKey(String apiKey) throws InvalidKeyException;

    boolean hasPermission(Principal principal, String... roles);
}
