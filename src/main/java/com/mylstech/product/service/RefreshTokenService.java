package com.mylstech.product.service;

import com.mylstech.product.config.JwtProperties;
import com.mylstech.product.exception.TokenRefreshException;
import com.mylstech.product.model.RefreshToken;
import com.mylstech.product.model.User;
import com.mylstech.product.repository.RefreshTokenRepository;
import com.mylstech.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken ( token );
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById ( userId )
                .orElseThrow ( () -> new RuntimeException ( "User not found with id: " + userId ) );

        // First, delete any existing tokens for this user
        refreshTokenRepository.deleteByUserId ( userId );

        // Create new refresh token
        RefreshToken refreshToken = RefreshToken.builder ( )
                .user ( user )
                .token ( UUID.randomUUID ( ).toString ( ) )
                .expiryDate ( Instant.now ( ).plusMillis ( jwtProperties.getRefreshExpirationMs ( ) ) )
                .revoked ( false )
                .build ( );

        return refreshTokenRepository.save ( refreshToken );
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if ( token.isRevoked ( ) ) {
            refreshTokenRepository.delete ( token );
            throw new TokenRefreshException ( token.getToken ( ), "Refresh token was revoked. Please sign in again" );
        }

        if ( token.getExpiryDate ( ).compareTo ( Instant.now ( ) ) < 0 ) {
            refreshTokenRepository.delete ( token );
            throw new TokenRefreshException ( token.getToken ( ), "Refresh token was expired. Please sign in again" );
        }

        return token;
    }

    @Transactional
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken ( token )
                .orElseThrow ( () -> new TokenRefreshException ( token, "Refresh token not found" ) );

        refreshToken.setRevoked ( true );
        refreshTokenRepository.save ( refreshToken );
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepository.deleteByUserId ( userId );
    }

    @Scheduled(fixedRate = 86400000) // Run once a day
    @Transactional
    public void cleanupExpiredTokens() {
        Instant now = Instant.now ( );
        refreshTokenRepository.deleteByExpiryDateLessThan ( now );
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken ( token )
                .orElseThrow ( () -> new TokenRefreshException ( token, "Refresh token not found" ) );

        refreshTokenRepository.delete ( refreshToken );
    }
}
