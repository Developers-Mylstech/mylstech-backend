package com.mylstech.product.exception;

public class DirectoryCreationException extends RuntimeException {
    public DirectoryCreationException(String s) {
        super ( s );
    }

    public DirectoryCreationException(String s, Exception ex) {
        super ( s, ex );
    }
}
