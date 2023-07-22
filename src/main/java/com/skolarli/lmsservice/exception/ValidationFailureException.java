package com.skolarli.lmsservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ValidationFailureException extends RuntimeException {
    private String resourceName;
    private String fieldName;

    public ValidationFailureException(String message) {
        super(message);
    }

    public ValidationFailureException(String resourceName, String fieldName, String message) {
        super(String.format("Validation failure for field %s in resource %s with message %s",
                resourceName, fieldName, message));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }

    public ValidationFailureException(String resourceName, String fieldName) {
        super(String.format("Validation failure for field %s in resource %s", resourceName,
                fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
