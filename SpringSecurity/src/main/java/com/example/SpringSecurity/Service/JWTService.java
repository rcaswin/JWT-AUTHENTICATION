package com.example.SpringSecurity.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    // Generate a secure key directly (No Encoding Issues)
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Include role in JWT

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // Set email as subject
                .setIssuedAt(new Date()) // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10-hour expiration
                .signWith(SignatureAlgorithm.HS256, key) // Sign with secret key
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Debugging: Print the token to ensure it is correctly formatted
        System.out.println("Token: " + token);
        if (token == null || token.split("\\.").length != 3) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Use the raw key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        return key; // Directly return the secret key
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}