package com.project.team4backend.domain.auth.dto.request;

import com.project.team4backend.domain.auth.entity.enums.Type;
import lombok.Builder;

public class EmailVerificationReqDTO {
    @Builder
    public record EmailSendReqDTO(
            String email,
            Type type
    ){
    }

    @Builder
    public record EmailVerifyReqDTO(
            String email,
            String authCode
    ){
    }
}
