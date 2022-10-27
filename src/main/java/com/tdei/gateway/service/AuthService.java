package com.tdei.gateway.service;

import com.tdei.gateway.config.ApplicationProperties;
import com.tdei.gateway.config.exception.handler.exceptions.InvalidCredentialsException;
import com.tdei.gateway.model.authclient.LoginModel;
import com.tdei.gateway.model.authclient.TokenResponse;
import com.tdei.gateway.model.authclient.UserProfile;
import com.tdei.gateway.service.contract.AuthServiceClient;
import com.tdei.gateway.service.contract.IAuthService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.Principal;
import java.util.Arrays;

@AllArgsConstructor
@Service("authService")
public class AuthService implements IAuthService {
    @Autowired
    private ApplicationProperties applicationProperties;

    public boolean hasPermission(Principal principal, String... roles) {
        String[] roleArray = roles;
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        Boolean satisfiedRoles = authServiceClient.hasPermission(user.getId(), null, Arrays.stream(roleArray).toList(), true);
        return satisfiedRoles;
    }

    public boolean hasAgencyPermission(Principal principal, String agencyId, String... roles) {
        String[] roleArray = roles;
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        Boolean satisfiedRoles = authServiceClient.hasPermission(user.getId(), agencyId, Arrays.stream(roleArray).toList(), true);
        return satisfiedRoles;
    }

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
    public UserProfile validateAccessToken(String token) {
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        return authServiceClient.validateAccessToken(token);
    }

    @Override
    public UserProfile validateApiKey(String apiKey) throws InvalidKeyException {
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        return authServiceClient.validateApiKey(apiKey);
    }
}