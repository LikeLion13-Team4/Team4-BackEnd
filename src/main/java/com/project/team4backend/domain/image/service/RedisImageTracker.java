package com.project.team4backend.domain.image.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.team4backend.domain.image.converter.ImageConverter;
import com.project.team4backend.domain.image.dto.internel.ImageInternelDTO;
import com.project.team4backend.domain.image.exception.ImageErrorCode;
import com.project.team4backend.domain.image.exception.ImageException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisImageTracker {

    private final RedisTemplate<String, Object> imageRedisTemplate;
    private final ObjectMapper objectMapper;

    // 생성자에서 @Qualifier로 imageRedisTemplate 주입
    public RedisImageTracker(
            @Qualifier("imageRedisTemplate") RedisTemplate<String, Object> imageRedisTemplate,
            ObjectMapper objectMapper
    ) {
        this.imageRedisTemplate = imageRedisTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String REDIS_KEY_PREFIX = "images:";
    private static final long TRACKING_DURATION_HOURS = 24; // 24시간 추적

    /**
     * Redis에 이미지 추적 정보 저장
     */
    public void save(String fileKey) {
        try {
            String redisKey = REDIS_KEY_PREFIX + fileKey;
            ImageInternelDTO.ImageTrackingResDTO imageTrackingResDTO = ImageConverter.toImageTrackingResDTO(fileKey);

            imageRedisTemplate.opsForValue().set(redisKey, imageTrackingResDTO, TRACKING_DURATION_HOURS, TimeUnit.HOURS);

        } catch (Exception e) {
            throw new ImageException(ImageErrorCode.REDIS_SAVE_FAIL);
        }
    }

    /**
     * Redis에서 이미지 추적 정보 제거
     */
    public void remove(String fileKey) {
        try {
            String redisKey = REDIS_KEY_PREFIX + fileKey;
            imageRedisTemplate.delete(redisKey);
        } catch (Exception e) {
            throw new ImageException(ImageErrorCode.REDIS_REMOVE_FAIL);
        }
    }

    /**
     * 모든 추적 중인 이미지 키 조회
     */
    public Set<String> getAllTrackedFileKeys() {
        try {
            Set<String> redisKeys = imageRedisTemplate.keys(REDIS_KEY_PREFIX + "*");
            return redisKeys.stream()
                    .map(key -> key.substring(REDIS_KEY_PREFIX.length()))
                    .collect(java.util.stream.Collectors.toSet());

        } catch (Exception e) {
            throw new ImageException(ImageErrorCode.REDIS_KEY_FETCH_FAIL);
        }
    }

    /**
     * 특정 시간 이전의 추적 정보 조회
     */
    public Set<String> getExpiredFileKeys(LocalDateTime expiredBefore) {
        try {
            Set<String> allKeys = getAllTrackedFileKeys();
            return allKeys.stream()
                    .filter(fileKey -> {
                        try {
                            String redisKey = REDIS_KEY_PREFIX + fileKey;
                            Object raw = imageRedisTemplate.opsForValue().get(redisKey);
                            if (raw == null) return false;

                            ImageInternelDTO.ImageTrackingResDTO info = objectMapper.convertValue(raw, ImageInternelDTO.ImageTrackingResDTO.class);

                            return info != null && info.createAt().isBefore(expiredBefore);
                        } catch (Exception e) {
                            log.warn("fileKey 만료 확인 중 에러 발생: {}", fileKey, e);
                            return false;
                        }
                    })
                    .collect(java.util.stream.Collectors.toSet());

        } catch (Exception e) {
            throw new ImageException(ImageErrorCode.REDIS_EXPIRED_FETCH_FAIL);
        }
    }
}