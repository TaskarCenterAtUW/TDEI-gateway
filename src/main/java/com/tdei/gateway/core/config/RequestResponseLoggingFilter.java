package com.tdei.gateway.core.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {
    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
        StringBuilder msg = new StringBuilder()
                .append(prefix)
                .append("[HTTP_Method:").append(request.getMethod()).append("]")
                .append("[Client_Address:").append(request.getRemoteAddr()).append("]");

        if (request.getQueryString() != null)
            msg.append("[URI:").append(request.getRequestURI() + "?" + request.getQueryString()).append("]");
        else
            msg.append("[URI:").append(request.getRequestURI()).append("]");


        log.info(msg.toString());
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix, long timeTaken) {
        StringBuilder msg = new StringBuilder()
                .append(prefix)
                .append("[HTTP_Status:").append(response.getStatus()).append("]")
                .append("[HTTP_Message:").append(HttpStatus.valueOf(response.getStatus()).getReasonPhrase()).append("]")
                .append("[Time_Taken_MS:").append(timeTaken).append("]");

        log.info(msg.toString());
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String requestId = String.valueOf(UUID.randomUUID());
        try {

            beforeRequest(request, response, requestId);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response, System.currentTimeMillis() - startTime, requestId);
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, String requestId) {
        if (log.isInfoEnabled()) {
            logRequestHeader(request, "Request -> " + "[Trace_Id:" + requestId + "] ");
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long timeTaken, String requestId) {
        if (log.isInfoEnabled()) {
            logResponse(response, "Response -> " + "[Trace_Id:" + requestId + "] ", timeTaken);
        }
    }
}
