package com.project.team4backend.domain.image.service.command;

import com.project.team4backend.domain.image.dto.request.ImageReqDTO;
import com.project.team4backend.domain.image.dto.response.ImageResDTO;

public interface ImageCommandService {
    ImageResDTO.PresignedUrlResDTO generatePresignedUrl(String email, ImageReqDTO.PresignedUrlReqDTO presignedUrl);

    ImageResDTO.PresignedUrlListResDTO generatePresignedUrlList(String email, ImageReqDTO.PresignedUrlListReqDTO presignedUrlListReqDTO);

    String commit(String email, String fileKey);

    void delete(String email, String fileKey);
}
