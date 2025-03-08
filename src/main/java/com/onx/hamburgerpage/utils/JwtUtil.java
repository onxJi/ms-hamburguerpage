package com.onx.hamburgerpage.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey SECRET_KEY;
    private static final long EXPIRATION_TIME = 86400000; // 24 horas

    public JwtUtil(@Value("${app.secretJwt}") String secretKey) {
        if (secretKey == null || secretKey.getBytes(StandardCharsets.UTF_8).length < 64) {
            throw new IllegalArgumentException("La clave secreta debe tener al menos 64 bytes (512 bits)");
        }
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY) // Usa directamente el objeto SecretKey
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Usa directamente el objeto SecretKey
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String email) {
        String userEmail = extractClaims(token).getSubject();
        return userEmail.equals(email) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}