package com.sixa.giveawayapp.common.security;

import com.sixa.giveawayapp.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class JwtUtil {
    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    @Value("${jwt.secret}")
    private String secretKey;

    private final long expirationTime = 1000 * 60 * 60; // 1 hour

    public String generateToken(int userId, Role role) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.name());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Extract all claims from the token
    private Claims extractClaims(String token) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        JwtParser parser = Jwts.parser()
                .verifyWith(key)
                .build();

        return parser.parseSignedClaims(token).getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String userId) {
        return (userId.equals(extractUserId(token)) && !isTokenExpired(token));
    }
}