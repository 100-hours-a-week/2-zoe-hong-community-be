package com.community.backend.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final Duration refreshTokenDuration = Duration.ofDays(1);

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(Long userId) {
        return "refresh_token:" + userId;
    }

    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(getKey(userId), refreshToken, refreshTokenDuration);
    }

    public Optional<String> find(Long userId) {
        String token = redisTemplate.opsForValue().get(getKey(userId));
        return Optional.ofNullable(token);
    }

    public boolean isValid(Long userId, String refreshToken) {
        return refreshToken.equals(redisTemplate.opsForValue().get(getKey(userId)));
    }

    public void delete(Long userId) {
        redisTemplate.delete(getKey(userId));
    }
}