package com.project.team4backend.domain.image.service.command;

import com.project.team4backend.domain.image.converter.ImageConverter;
import com.project.team4backend.domain.image.dto.request.ImageReqDTO;
import com.project.team4backend.domain.image.dto.response.ImageResDTO;
import com.project.team4backend.domain.image.exception.ImageErrorCode;
import com.project.team4backend.domain.image.exception.ImageException;
import com.project.team4backend.domain.image.service.RedisImageTracker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageCommandServiceImpl implements ImageCommandService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final RedisImageTracker redisImageTracker;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.presigned-url-duration:15}")
    private long presignedUrlDurationMinutes;

    @Value(("${aws.s3.region}"))
    private String region;

    /**
     * Presigned URL 발급
     * @param "fileExtension" 파일 확장자 (예: jpg, png)
     * @param "contentType" MIME 타입 (예: image/jpeg)
     * @return Presigned URL과 파일 키
     */
    @Override
    public ImageResDTO.PresignedUrlResDTO generatePresignedUrl(ImageReqDTO.PresignedUrlDTO presignedUrlDTO) {
        validateFileExtension(presignedUrlDTO.fileExtension());
        validateContentType(presignedUrlDTO.contentType());

        String fileKey = generateFileKey(presignedUrlDTO.fileExtension());

        try {
            PutObjectRequest putObjectRequest = ImageConverter.toPutObjectRequest(bucketName, fileKey, presignedUrlDTO.contentType());

            PutObjectPresignRequest presignRequest = ImageConverter.toPutObjectPresignRequest(Duration.ofMinutes(presignedUrlDurationMinutes), putObjectRequest);

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();

            // Redis에 추적 정보 저장
            redisImageTracker.save(fileKey);

            return ImageConverter.toPresignedUrlResDTO(presignedUrl,fileKey);

        } catch (Exception e) {
            throw new ImageException(ImageErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }
    /**
     * 파일 존재 여부 확인
     */
    private boolean isFileExists(String fileKey) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            s3Client.headObject(headRequest);
            return true;

        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            throw new ImageException(ImageErrorCode.REDIS_KEY_FETCH_FAIL);
        }
    }

    /**
     * 파일 키 생성
     */
    private String generateFileKey(String fileExtension) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        return String.format("images/%s_%s.%s", timestamp, uuid, fileExtension);
    }

    /**
     * 파일 확장자 검증
     */
    private void validateFileExtension(String fileExtension) {
        if (fileExtension == null || fileExtension.trim().isEmpty()) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_EXTENSION);
        }

        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "webp"};
        String lowerExtension = fileExtension.toLowerCase();

        for (String allowed : allowedExtensions) {
            if (allowed.equals(lowerExtension)) {
                return;
            }
        }

        throw new ImageException(ImageErrorCode.IMAGE_INVALID_CONTENT_TYPE);
    }

    /**
     * Content Type 검증
     */
    private void validateContentType(String contentType) {
        if (contentType == null || contentType.trim().isEmpty()) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_CONTENT_TYPE);
        }

        String[] allowedTypes = {"image/jpeg", "image/png", "image/gif", "image/webp"};

        for (String allowed : allowedTypes) {
            if (allowed.equals(contentType)) {
                return;
            }
        }

        throw new ImageException(ImageErrorCode.IMAGE_INVALID_CONTENT_TYPE);
    }
}