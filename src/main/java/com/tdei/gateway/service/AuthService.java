package com.tdei.gateway.service;

import com.tdei.gateway.config.ApplicationProperties;
import com.tdei.gateway.config.exception.handler.exceptions.InvalidCredentialsException;
import com.tdei.gateway.model.authclient.LoginModel;
import com.tdei.gateway.model.authclient.TokenResponse;
import com.tdei.gateway.service.contract.AuthServiceClient;
import com.tdei.gateway.service.contract.IAuthService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;

@AllArgsConstructor
@Service
public class AuthService implements IAuthService {
    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public TokenResponse authenticate(LoginModel loginModel) {
        try {
            AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
            TokenResponse tokenResponse = authServiceClient.authenticate(loginModel);
            return tokenResponse;
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new InvalidCredentialsException("Invalid Credentials");
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TokenResponse validateAccessToken(String token) {
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        return authServiceClient.validateAccessToken(token);
    }

    @Override
    public TokenResponse validateApiKey(String apiKey) throws InvalidKeyException {
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        return authServiceClient.validateApiKey(apiKey);
    }
}