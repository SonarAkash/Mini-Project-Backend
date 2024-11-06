package com.TY_CSE.Project.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generates a 512-bit key

    @Value("${jwt.expiration}")
    private long expiration; // Token expiration time in milliseconds


    // Generate a token for the given email
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    // Create a token with claims and subject
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Use configured expiration time
                .signWith(secretKey, SignatureAlgorithm.HS512) // Sign with the generated signing key
                .compact(); // Compact to get the JWT string
    }

    // Extract username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extract subject (username) from claims
    }

    // Validate token against the email
    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token)); // Validate username and expiration
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract expiration date
    }

    // Extract claims using a claims resolver function
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder() // Use parserBuilder() to get the claims
                .setSigningKey(secretKey) // Set signing key
                .build()
                .parseClaimsJws(token) // Parse the JWT
                .getBody();
        return claimsResolver.apply(claims); // Apply the resolver function to claims
    }
}
