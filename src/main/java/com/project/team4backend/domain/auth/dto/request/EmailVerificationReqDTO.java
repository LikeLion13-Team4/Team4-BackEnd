package com.project.team4backend.domain.auth.dto.request;

import lombok.Builder;

public class EmailVerificationReqDTO {
    @Builder
    public record EmailSendReqDTO(
            String email
    ){
    }

    @Builder
    public record EmailVerifyReqDTO(
            String email,
            String authCode
    ){
    }
}
