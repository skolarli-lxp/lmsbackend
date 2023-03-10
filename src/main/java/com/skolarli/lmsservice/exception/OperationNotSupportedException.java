package com.skolarli.lmsservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OperationNotSupportedException extends RuntimeException {
    private String resourceName;
    private String fieldName;

    public OperationNotSupportedException(String message) {
        super(message);
    }
}
