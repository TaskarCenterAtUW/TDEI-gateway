package com.tdei.gateway.core.service.auth;

import com.tdei.gateway.core.config.ApplicationProperties;
import com.tdei.gateway.core.config.exception.handler.exceptions.InvalidCredentialsException;
import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.core.service.auth.contract.AuthServiceClient;
import com.tdei.gateway.core.service.auth.contract.IAuthService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.Principal;
import java.util.Arrays;

@RequiredArgsConstructor
@Service("authService")
public class AuthService implements IAuthService {
    private final ApplicationProperties applicationProperties;

    public boolean hasPermission(Principal principal, String... permissions) {
        String[] roleArray = permissions;
        UserProfile user = (UserProfile) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        AuthServiceClient authServiceClient = AuthServiceClient.connect(applicationProperties.getAuthServerUrl());
        Boolean satisfiedRoles = authServiceClient.hasPermission(user.getId(), null, Arrays.stream(roleArray).toList(), true);
        return satisfiedRoles;
    }

    public boolean hasAgencyPermission(Principal principal, String agencyId, String... permissions) {
        String[] roleArray = permissions;
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