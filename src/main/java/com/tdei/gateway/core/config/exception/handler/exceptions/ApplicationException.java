package com.tdei.gateway.core.config.exception.handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApplicationException(String message) {
        super(message);
    }
}
