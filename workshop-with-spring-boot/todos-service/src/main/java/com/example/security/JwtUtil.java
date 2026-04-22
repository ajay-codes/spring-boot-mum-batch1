package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // Generate JWT token
    public String generateToken(String username, List<String> roles) {
        List<String> safeRoles = (roles == null)
                ? Collections.emptyList()
                : roles.stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(role -> !role.isEmpty())
                        .distinct()
                        .toList();

        return Jwts.builder()
                .subject(username)
                .claim("roles", safeRoles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    // Extract username from token
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extract roles from token
    public List<String> getRoles(String token) {
        Object rolesClaim = getClaims(token).get("roles");
        if (!(rolesClaim instanceof List<?> roles)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>(roles.size());
        for (Object role : roles) {
            if (role instanceof String roleName) {
                result.add(roleName);
            }
        }
        return result;
    }

    // Validate token
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
