package com.mylstech.product.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mylstech.product.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper ( );
        this.objectMapper.registerModule ( new JavaTimeModule ( ) );
        this.objectMapper.disable ( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // Set response status and content type
        response.setStatus ( HttpServletResponse.SC_UNAUTHORIZED );
        response.setContentType ( MediaType.APPLICATION_JSON_VALUE );

        // Create error response object
        ErrorResponse errorResponse = new ErrorResponse (
                HttpStatus.UNAUTHORIZED.value ( ),
                authException.getMessage ( ) != null ? authException.getMessage ( ) : "Unauthorized: Authentication token is missing or invalid",
                LocalDateTime.now ( )
        );

        // Write error response as JSON
        objectMapper.writeValue ( response.getOutputStream ( ), errorResponse );
    }
}