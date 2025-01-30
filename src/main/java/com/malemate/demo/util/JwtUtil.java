package com.malemate.demo.util;

import com.malemate.demo.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key getSigningKey() {
        // Ensure secret key is at least 32 bytes long for HMAC-SHA256
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generate JWT token for the user
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Set user email as subject
                .claim("roles", user.getUserType().name()) // Add roles claim
                .setIssuedAt(new Date()) // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration (10 hours)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with key
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate token
    public boolean validateToken(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // In extractClaim method
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class)); // Extract roles from JWT
    }

    // Generic method to extract claims
    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        Claims claims = Jwts.parser() // Use parserBuilder instead of deprecated parser()
                .setSigningKey(getSigningKey()) // Set signing key
                .build()
                .parseClaimsJws(token) // Parse token
                .getBody(); // Get claims
        return claimsResolver.resolve(claims);
    }

    // Functional interface for extracting claims
    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}
