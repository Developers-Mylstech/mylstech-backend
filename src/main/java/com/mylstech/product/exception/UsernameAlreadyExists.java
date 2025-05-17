package com.mylstech.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String s) {
        super ( s );
    }
}
