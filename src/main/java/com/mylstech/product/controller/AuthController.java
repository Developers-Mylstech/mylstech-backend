package com.mylstech.product.controller;

import com.mylstech.product.dto.request.*;
import com.mylstech.product.dto.response.JwtResponse;
import com.mylstech.product.dto.response.TokenRefreshResponse;
import com.mylstech.product.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok ( authService.authenticateUser ( loginRequest ) );
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok ( authService.registerUser ( signupRequest ) );
    }

    @PostMapping("/verify")
    public ResponseEntity<JwtResponse> verifyUser(@Valid @RequestBody EmailVerificationRequest request) {
        return ResponseEntity.ok ( authService.verifyUser ( request ) );
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody ResendOtpRequest request) {
        return ResponseEntity.ok ( authService.sendOtp ( request.getEmail ( ) ) );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok ( authService.refreshToken ( request ) );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(@Valid @RequestBody TokenRefreshRequest request) {
        authService.logout ( request.getRefreshToken ( ) );
        return ResponseEntity.ok ( ).build ( );
    }
}
