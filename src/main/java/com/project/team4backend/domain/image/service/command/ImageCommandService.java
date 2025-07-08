package com.project.team4backend.domain.image.service.command;

import com.project.team4backend.domain.image.dto.request.ImageReqDTO;
import com.project.team4backend.domain.image.dto.response.ImageResDTO;

public interface ImageCommandService {
    ImageResDTO.PresignedUrlResDTO generatePresignedUrl(ImageReqDTO.PresignedUrlDTO presignedUrl);

}
