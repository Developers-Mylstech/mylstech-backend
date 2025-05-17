package com.mylstech.product.exception;

import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.NOT_FOUND.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.UNAUTHORIZED.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.UNAUTHORIZED );
    }

    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<ErrorResponse> handleResourceInUseException(
            ResourceInUseException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.CONFLICT.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.CONFLICT );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.INTERNAL_SERVER_ERROR.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(
            UsernameAlreadyExists ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.IM_USED.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.IM_USED );
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<ErrorResponse> handleOtpInvalidException(
            OtpInvalidException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.BAD_REQUEST.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefreshException(
            TokenRefreshException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.FORBIDDEN.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.FORBIDDEN );
    }

    @ExceptionHandler(DirectoryCreationException.class)
    public ResponseEntity<ErrorResponse> handleDirectoryCreationException(
            DirectoryCreationException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.NOT_IMPLEMENTED.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.NOT_IMPLEMENTED );
    }

    @ExceptionHandler(InvalidFileNameException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileNameException(
            InvalidFileNameException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.BAD_REQUEST.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntryException(
            DuplicateEntryException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.CONFLICT.value ( ),
                ex.getMessage ( ),
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.CONFLICT );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {

        String message = "Database error occurred";

        // Check if it's a duplicate key error
        if ( ex.getCause ( ) instanceof ConstraintViolationException ) {
            String exMessage = ex.getMessage ( );
            if ( exMessage != null && exMessage.contains ( "Duplicate entry" ) ) {
                message = "A record with the same unique identifier already exists";
            }
        }

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.CONFLICT.value ( ),
                message,
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.CONFLICT );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<> ( );
        ex.getBindingResult ( ).getAllErrors ( ).forEach ( error -> {
            String fieldName = ((FieldError) error).getField ( );
            String errorMessage = error.getDefaultMessage ( );
            errors.put ( fieldName, errorMessage );
        } );

        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.BAD_REQUEST.value ( ),
                "Validation failed: " + errors,
                LocalDateTime.now ( )
        );

        return new ResponseEntity<> ( errorResponse, HttpStatus.BAD_REQUEST );
    }


}