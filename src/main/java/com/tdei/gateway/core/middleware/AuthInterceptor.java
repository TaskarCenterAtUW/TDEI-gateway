package com.tdei.gateway.core.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdei.gateway.core.model.authclient.UserProfile;
import com.tdei.gateway.core.service.auth.AuthService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public
//@Order(Ordered.HIGHEST_PRECEDENCE)
class AuthInterceptor extends OncePerRequestFilter {

    private static final String[] AUTH_WHITELIST = {
            "/health/ping",
            "/authenticate",
            "/swagger-resources",
            "/swagger-ui",
            "/api-docs",
            "/webjars",
            "/favicon"
    };
    private final AuthService authService;

    private void setSecurityContext(UserProfile userProfile) {
        Authentication auth = new UsernamePasswordAuthenticationToken(userProfile, null, getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Collection<GrantedAuthority> getAuthorities() {
        //All authenticated user are application user
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = () -> "TDEI_USER";
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(AUTH_WHITELIST).anyMatch(x -> path.contains(x))
                || path.equals("/"); //This fix to redirect to swagger doc
        //return path.contains("/public/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = request;
        HttpServletResponse res = response;
        UserProfile userProfile = null;
        String authorizationKey = req.getHeader("Authorization");
        if (authorizationKey == null) {
            String apiKey = req.getHeader("x-api-key");
            if (apiKey != null) {
                try {
                    userProfile = authService.validateApiKey(apiKey);
                    setSecurityContext(userProfile);
                } catch (FeignException e) {
                    if (e.status() == HttpStatus.NOT_FOUND.value()) {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API-KEY");
                        return;
                    }
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    response.getOutputStream().write(new ObjectMapper().writeValueAsString("Internal Server Error").getBytes());
                    return;
                } catch (Exception e) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    response.getOutputStream().write(new ObjectMapper().writeValueAsString("Internal Server Error").getBytes());
                    return;
                }

            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                response.getOutputStream().write(new ObjectMapper().writeValueAsString("Please provide API-KEY / Access Token").getBytes());
                return;
            }
        } else {
            try {
                if (authorizationKey.contains("Bearer")) {
                    authorizationKey = authorizationKey.split("Bearer")[1];
                }
                userProfile = authService.validateAccessToken(authorizationKey.trim());
                setSecurityContext(userProfile);
            } catch (FeignException e) {
                if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    response.getOutputStream().write(new ObjectMapper().writeValueAsString("Invalid Access Token").getBytes());
                    return;
                }
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                response.getOutputStream().write(new ObjectMapper().writeValueAsString("Internal Server Error").getBytes());
                return;
            } catch (Exception e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                response.getOutputStream().write(new ObjectMapper().writeValueAsString("Internal Server Error").getBytes());
                return;
            }
        }
        filterChain.doFilter(req, res);
    }
}