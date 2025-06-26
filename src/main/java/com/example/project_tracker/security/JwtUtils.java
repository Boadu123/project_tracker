package com.example.project_tracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating, parsing, and validating JWT tokens.
 * <p>
 * Uses HMAC SHA key signing for token security.
 * Tokens are used to authenticate users within the application.
 */
@Component
public class JwtUtils {

    // Secret key used to sign JWTs
    private final String jwtSecret = "projectTrackerSecretKeyIsAnActiveDataThatNeedToBeUsed";

    // Default token expiration in milliseconds (24 hours)
    private final long jwtExpirationMs = 86400000;

    /**
     * Generates the cryptographic key used to sign the JWT.
     *
     * @return the signing {@link Key}
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for a given user's email.
     * The token is valid for 15 minutes.
     *
     * @param email the user's email to include as the subject
     * @return a signed JWT token string
     */
    public String generateToken(String email) {
        long jwtExpirationMs = 15 * 60 * 1000; // 15 minutes

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the user's email (subject) from the provided JWT token.
     *
     * @param token the JWT token to decode
     * @return the email (subject) embedded in the token
     * @throws JwtException if the token is invalid or expired
     */
    public String getEmailFromJwt(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the JWT token's structure, signature, and expiration.
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid; {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
