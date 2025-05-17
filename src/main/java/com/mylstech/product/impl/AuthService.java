package com.mylstech.product.impl;

import com.mylstech.product.dto.request.EmailVerificationRequest;
import com.mylstech.product.dto.request.LoginRequest;
import com.mylstech.product.dto.request.SignupRequest;
import com.mylstech.product.dto.request.TokenRefreshRequest;
import com.mylstech.product.dto.response.JwtResponse;
import com.mylstech.product.dto.response.TokenRefreshResponse;
import com.mylstech.product.exception.OtpInvalidException;
import com.mylstech.product.exception.TokenRefreshException;
import com.mylstech.product.exception.UsernameAlreadyExists;
import com.mylstech.product.mapper.UserMapper;
import com.mylstech.product.model.RefreshToken;
import com.mylstech.product.model.User;
import com.mylstech.product.repository.UserRepository;
import com.mylstech.product.security.JwtTokenUtil;
import com.mylstech.product.security.UserSecurityDetails;
import com.mylstech.product.service.OtpService;
import com.mylstech.product.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String BEARER = "Bearer";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail ( loginRequest.getEmail ( ) )
                .orElseThrow ( () -> new UsernameNotFoundException ( "User not found" ) );

        if ( ! user.isVerified ( ) ) {
            throw new UsernameNotFoundException ( "You have to verify your email first" );
        }

        Authentication authentication = authenticationManager.authenticate (
                new UsernamePasswordAuthenticationToken ( loginRequest.getEmail ( ), loginRequest.getPassword ( ) ) );

        SecurityContextHolder.getContext ( ).setAuthentication ( authentication );
        String jwt = jwtTokenUtil.generateToken ( authentication );

        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken ( user.getUserId ( ) );

        return JwtResponse.builder ( )
                .accessToken ( jwt )
                .refreshToken ( refreshToken.getToken ( ) )
                .tokenType ( BEARER )
                .build ( );
    }

    public String registerUser(SignupRequest signupRequest) {
        if ( Boolean.TRUE.equals ( userRepository.existsByEmail ( signupRequest.getEmail ( ) ) ) ) {
            throw new UsernameAlreadyExists ( "Email is already in use!" );
        }

        // Create new user using mapper
        User user = userMapper.toEntity ( signupRequest );

        // Save user
        User savedUser = userRepository.save ( user );

        // Return user response
        return otpService.sendOtp ( savedUser.getEmail ( ) );
    }

    @Transactional
    public JwtResponse verifyUser(EmailVerificationRequest request) {
        if ( ! otpService.verifyOtp ( request.getEmail ( ), request.getOtp ( ) ) ) {
            throw new OtpInvalidException ( "Otp not match try again" );
        }

        User user = userRepository.findByEmail ( request.getEmail ( ) )
                .orElseThrow ( () -> new UsernameNotFoundException ( "User not found" ) );

        user.setVerified ( true );
        userRepository.save ( user );

        UserDetails details = new UserSecurityDetails ( user );
        String jwt = jwtTokenUtil.generateToken ( details );

        // Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken ( user.getUserId ( ) );

        return JwtResponse.builder ( )
                .accessToken ( jwt )
                .refreshToken ( refreshToken.getToken ( ) )
                .tokenType ( BEARER )
                .build ( );
    }

    public String sendOtp(String email) {
        userRepository.findByEmail ( email )
                .orElseThrow ( () -> new UsernameNotFoundException ( "Signup first" ) );
        return otpService.sendOtp ( email );
    }

    @Transactional
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken ( );

        return refreshTokenService.findByToken ( requestRefreshToken )
                .map ( refreshTokenService::verifyExpiration )
                .map ( RefreshToken::getUser )
                .map ( user -> {
                    UserDetails userDetails = new UserSecurityDetails ( user );
                    String token = jwtTokenUtil.generateToken ( userDetails );

                    return TokenRefreshResponse.builder ( )
                            .accessToken ( token )
                            .refreshToken ( requestRefreshToken )
                            .tokenType ( BEARER )
                            .build ( );
                } )
                .orElseThrow ( () -> new TokenRefreshException ( requestRefreshToken, "Refresh token not found" ) );
    }

    @Transactional
    public void logout(String refreshToken) {
        if ( refreshToken != null && ! refreshToken.isEmpty ( ) ) {
            refreshTokenService.deleteRefreshToken ( refreshToken );
        }
    }
}
