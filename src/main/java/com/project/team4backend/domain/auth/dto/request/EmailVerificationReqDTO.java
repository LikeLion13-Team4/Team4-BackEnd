package com.project.team4backend.domain.auth.dto.request;

import com.project.team4backend.domain.auth.entity.enums.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public class EmailVerificationReqDTO {
    @Builder
    public record EmailSendReqDTO(
            String email,
            @Schema(description = "인증 요청 목적", example = "SIGNUP")
            Type type
    ){
    }

    @Builder
    public record EmailVerifyReqDTO(
            String email,
            String authCode,
            @Schema(description = "인증 요청 목적", example = "SIGNUP")
            Type type
    ){
    }
}
