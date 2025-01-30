package com.malemate.demo.util;

import com.malemate.demo.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Load the secret key from application.properties for better security
    @Value("${jwt.secret.key}")
    private String secretKey;

    // Generate the signing key
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generate a JWT token for the user
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Set the subject (email)
                .setIssuedAt(new Date()) // Set the issued date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Set expiration (10 hours)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with the key
                .compact();
    }

    // Extract the email from the token
    public String extractEmail(String token) {
        return Jwts.parser() // Using the new parserBuilder
                .setSigningKey(getSigningKey()) // Set the signing key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Extract the subject (email)
    }

    // Validate the token with email
    public boolean validateToken(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    // Extract the expiration date from the token
    private Date extractExpirationDate(String token) {
        return Jwts.parser() // Using the new parserBuilder
                .setSigningKey(getSigningKey()) // Set the signing key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration(); // Extract expiration
    }
}
