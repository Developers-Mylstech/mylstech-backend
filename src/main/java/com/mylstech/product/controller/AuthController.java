package com.mylstech.product.controller;

import com.mylstech.product.dto.request.*;
import com.mylstech.product.dto.response.JwtResponse;
import com.mylstech.product.dto.response.TokenRefreshResponse;
import com.mylstech.product.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication and user management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user with email and password, and returns JWT tokens for authorization"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found or not verified")
    })
    public ResponseEntity<JwtResponse> authenticateUser(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok ( authService.authenticateUser ( loginRequest ) );
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Register new user",
            description = "Registers a new user with the provided details and sends verification OTP"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful, verification OTP sent"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    public ResponseEntity<String> registerUser(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok ( authService.registerUser ( signupRequest ) );
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Verify user email",
            description = "Verifies user email with OTP and returns JWT tokens for authorization"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verification successful"),
            @ApiResponse(responseCode = "400", description = "Invalid OTP or request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<JwtResponse> verifyUser(
            @Parameter(description = "Email verification details with OTP", required = true)
            @Valid @RequestBody EmailVerificationRequest request) {
        return ResponseEntity.ok ( authService.verifyUser ( request ) );
    }

    @PostMapping("/send-otp")
    @Operation(
            summary = "Resend verification OTP",
            description = "Resends verification OTP to the specified email address"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<String> sendOtp(
            @Parameter(description = "Email to send OTP to", required = true)
            @Valid @RequestBody ResendOtpRequest request) {
        return ResponseEntity.ok ( authService.sendOtp ( request.getEmail ( ) ) );
    }

    @PostMapping("/refresh-token")
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Parameter(description = "Refresh token details", required = true)
            @Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok ( authService.refreshToken ( request ) );
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Invalidates the refresh token, effectively logging out the user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Void> logoutUser(
            @Parameter(description = "Refresh token to invalidate", required = true)
            @Valid @RequestBody TokenRefreshRequest request) {
        authService.logout ( request.getRefreshToken ( ) );
        return ResponseEntity.ok ( ).build ( );
    }
}
