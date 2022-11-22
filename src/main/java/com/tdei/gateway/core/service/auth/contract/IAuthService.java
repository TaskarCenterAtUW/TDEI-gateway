package com.tdei.gateway.core.service.auth.contract;

import com.tdei.gateway.core.model.authclient.LoginModel;
import com.tdei.gateway.core.model.authclient.TokenResponse;
import com.tdei.gateway.core.model.authclient.UserProfile;

import java.security.InvalidKeyException;
import java.security.Principal;

public interface IAuthService {
    /**
     * Validates the login credentials and returns Token on successful validation
     *
     * @param loginModel - Login credentials
     * @return TokenResponse
     */
    TokenResponse authenticate(LoginModel loginModel);

    /**
     * Validates the Access token and returns UserProfile on successful validation
     *
     * @param token - Access token
     * @return UserProfile
     */
    UserProfile validateAccessToken(String token);

    /**
     * Validates the Api Key and returns UserProfile on successful validation
     *
     * @param apiKey - Api Key
     * @return TokenResponse
     */
    UserProfile validateApiKey(String apiKey) throws InvalidKeyException;

    /**
     * Verifies the permission for given userId and permissions
     *
     * @param principal   - Logged user context
     * @param permissions - List of permissions to verify
     * @return
     */
    boolean hasPermission(Principal principal, String... permissions);

    /**
     * Verifies the permission for given userId, tdeiOrgId and permissions
     *
     * @param principal-   Logged user context
     * @param tdeiOrgId-   TdeiOrgId
     * @param permissions- List of permissions to verify
     * @return
     */
    boolean hasOrgPermission(Principal principal, String tdeiOrgId, String... permissions);
}
