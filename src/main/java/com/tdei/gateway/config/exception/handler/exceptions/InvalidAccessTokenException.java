package com.tdei.gateway.config.exception.handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidAccessTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
