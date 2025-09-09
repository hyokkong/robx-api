package com.rbox.auth.application.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rbox.user.adapter.out.persistence.repository.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 토큰을 생성하고 검증하는 컴포넌트.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationSeconds;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserEntity user) {
        long now = System.currentTimeMillis();
        Date exp = new Date(now + expirationSeconds * 1000);
        return Jwts.builder()
                .setSubject(String.valueOf(user.id()))
                .claim("id", user.id())
                .claim("nickname", user.nick())
                .claim("email", user.email())
                .setIssuedAt(new Date(now))
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        long now = System.currentTimeMillis();
        Date exp = new Date(now + expirationSeconds * 1000);
        return Jwts.builder()
                .setSubject(String.valueOf(user.id()))
                .setIssuedAt(new Date(now))
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }
}
