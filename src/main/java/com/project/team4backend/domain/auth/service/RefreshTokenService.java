package com.project.team4backend.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(1); // 1일

    public void saveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(getKey(email), refreshToken, REFRESH_TOKEN_EXPIRATION);
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(getKey(email));
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(getKey(email));
    }

    private String getKey(String email) {
        return "refresh:" + email;
    }
}
