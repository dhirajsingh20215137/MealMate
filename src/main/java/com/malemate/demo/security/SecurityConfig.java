//package com.malemate.demo.security;
//
//import io.jsonwebtoken.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Date;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.Set;
//
//@Configuration
//public class SecurityConfig {
//
//    @Value("${jwt.secret.key}") // Secret key for signing JWT
//    private String secretKey;
//
//    @Value("${jwt.expiration.time}") // JWT expiration time in milliseconds
//    private long expirationTime;
//
//    // Store blacklisted tokens for logout
//    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
//
//    // Generate JWT token for a user
//    public String generateToken(String username, String role) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("role", role) // Add role claim
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//    }
//
//    // Extract role claim from token
//    public String extractRole(String token) {
//        return extractClaim(token, claims -> claims.get("role", String.class));
//    }
//
//    // Extract username (subject) from JWT token
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // Extract expiration date from JWT token
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    // Generic method to extract a claim
//    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
//        Claims claims = extractAllClaims(token);
//        return claimsResolver.resolve(claims);
//    }
//
//    // Extract all claims from JWT
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser() // Fix deprecated `parser()`
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    // Check if JWT token is expired
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // Validate JWT token for a user
//    public boolean validateToken(String token, String username) {
//        return (!blacklistedTokens.contains(token)) // Check if token is blacklisted
//                && username.equals(extractUsername(token)) // Check if username matches
//                && !isTokenExpired(token); // Check expiration
//    }
//
//    // Logout by blacklisting the token
//    public void logout(String token) {
//        blacklistedTokens.add(token); // Add token to blacklist
//    }
//
//    // Custom functional interface for claim resolution
//    @FunctionalInterface
//    public interface ClaimsResolver<T> {
//        T resolve(Claims claims);
//    }
//}
