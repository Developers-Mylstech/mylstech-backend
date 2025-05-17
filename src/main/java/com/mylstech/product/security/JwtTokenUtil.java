package com.mylstech.product.security;

import com.mylstech.product.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final JwtProperties jwtProperties;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal ( );
        return generateToken ( userDetails );
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<> ( );
        return createToken ( claims, userDetails.getUsername ( ), jwtProperties.getExpirationMs ( ) );
    }

    // New method for creating tokens with custom expiration
    private String createToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date ( );
        Date expiryDate = new Date ( now.getTime ( ) + expirationMs );

        return Jwts.builder ( )
                .setClaims ( claims )
                .setSubject ( subject )
                .setIssuer ( jwtProperties.getIssuer ( ) )
                .setIssuedAt ( now )
                .setExpiration ( expiryDate )
                .signWith ( getSigningKey ( ), SignatureAlgorithm.HS512 )
                .compact ( );
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret ( ).getBytes ( StandardCharsets.UTF_8 );
        return Keys.hmacShaKeyFor ( keyBytes );
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken ( token, Claims::getSubject );
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken ( token, Claims::getExpiration );
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken ( token );
        return claimsResolver.apply ( claims );
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder ( )
                .setSigningKey ( getSigningKey ( ) )
                .build ( )
                .parseClaimsJws ( token )
                .getBody ( );
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken ( token );
        return (username.equals ( userDetails.getUsername ( ) ) && ! isTokenExpired ( token ));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder ( ).setSigningKey ( getSigningKey ( ) ).build ( ).parseClaimsJws ( token );
            return ! isTokenExpired ( token );
        }
        catch ( JwtException | IllegalArgumentException e ) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken ( token );
        return expiration.before ( new Date ( ) );
    }
}