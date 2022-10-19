package com.tdei.gateway.middleware;

import com.tdei.gateway.model.MutableHttpServletRequest;
import com.tdei.gateway.service.AuthService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class AuthInterceptor extends OncePerRequestFilter {

    private static final String[] AUTH_WHITELIST = {
            "/authenticate",
            "/swagger-resources",
            "/swagger-ui",
            "/api-docs",
            "/webjars",
            "/favicon"
    };
    @Autowired
    AuthService authService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(AUTH_WHITELIST).anyMatch(x -> path.contains(x));
        //return path.contains("/public/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = request;
        HttpServletResponse res = response;
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(req);

        String authorizationKey = req.getHeader("Authorization");
        if (authorizationKey == null) {
            String apiKey = req.getHeader("x-api-key");
            if (apiKey != null) {
                try {
                    authService.validateApiKey(apiKey);
                } catch (FeignException e) {
                    if (e.status() == HttpStatus.NOT_FOUND.value()) {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid API-KEY");
                        return;
                    }
                    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
                    return;
                } catch (Exception e) {
                    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
                    return;
                }

            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Provide API-KEY / Access Token");
                return;
            }
        } else {
            try {
                if (authorizationKey.contains("Bearer")) {
                    authorizationKey = authorizationKey.split("Bearer")[1];
                }
                authService.validateAccessToken(authorizationKey.trim());
            } catch (FeignException e) {
                if (e.status() == HttpStatus.NOT_FOUND.value()) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Access Token");
                    return;
                }
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
                return;
            } catch (Exception e) {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
                return;
            }
        }
        filterChain.doFilter(mutableRequest, res);
    }
}