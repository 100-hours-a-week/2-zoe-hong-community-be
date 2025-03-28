package com.community.backend.security;

import com.community.backend.dto.JwtDTO;
import com.community.backend.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final JwtDTO jwt;
    private SecretKey key;

    public JwtUtil(JwtDTO jwt) {
        this.jwt = jwt;
    }

    @PostConstruct
    public void init() {
        // Base64 디코딩
        byte[] decodedKey = Base64.getDecoder().decode(jwt.getSecret());
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }
    
    public String createToken(UserDTO user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .signWith(key, Jwts.SIG.HS256) // 서명 알고리즘 명시
                .subject(user.getNickname())
                .claim("id", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwt.getExpiration())))
                .compact();
    }

    public String createRefreshToken(UserDTO user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .signWith(key, Jwts.SIG.HS256)
                .subject(user.getNickname())
                .claim("id", user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwt.getRefreshExpiration())))
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}