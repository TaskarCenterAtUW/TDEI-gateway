package com.tdei.gateway.core.config.exception.handler.exceptions;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MetadataValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private byte[] errorData;

    public MetadataValidationException(String message, byte[] errorData) {
        super(message);
        this.errorData = errorData;
    }
}
