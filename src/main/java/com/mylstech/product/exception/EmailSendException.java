package com.mylstech.product.exception;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String failedToSendEmail, Exception e) {
        super(failedToSendEmail,e);
    }
}
