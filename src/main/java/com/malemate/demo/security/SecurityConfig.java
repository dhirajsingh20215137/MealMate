package com.malemate.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret.key}") // The secret key used for signing the JWT
    private String secretKey;

    @Value("${jwt.expiration.time}") // JWT expiration time in milliseconds
    private long expirationTime;

    // Generate a JWT token for a given username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // Set the current date as the issue date
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Set expiration time
                .signWith(SignatureAlgorithm.HS512, secretKey) // Sign with the secret key
                .compact();
    }

    // Extract the username (subject) from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extract subject (username) from the token
    }

    // Extract the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract expiration from the token
    }

    // Extract a claim (like username or roles) from the JWT token
    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        Claims claims = extractAllClaims(token); // Get all claims from the token
        return claimsResolver.resolve(claims); // Return the requested claim
    }

    // Extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Use parserBuilder instead of deprecated parser
                .setSigningKey(secretKey) // Use the secret key to verify the token
                .build()
                .parseClaimsJws(token) // Parse the token and get claims
                .getBody();
    }

    // Validate if the JWT token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Compare expiration with the current date
    }

    // Validate the JWT token for a specific user
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token)); // Check if username matches and token is not expired
    }

    // Custom functional interface to resolve claims from the token
    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}
