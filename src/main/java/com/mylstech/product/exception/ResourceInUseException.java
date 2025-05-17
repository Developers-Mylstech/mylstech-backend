package com.mylstech.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceInUseException extends RuntimeException {

    public ResourceInUseException(String message) {
        super ( message );
    }

    public ResourceInUseException(String resourceName, String usedBy) {
        super ( String.format ( "%s cannot be deleted as it is being used by %s", resourceName, usedBy ) );
    }
}