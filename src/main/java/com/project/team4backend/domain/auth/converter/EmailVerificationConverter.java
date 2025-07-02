package com.project.team4backend.domain.auth.converter;

import com.project.team4backend.domain.auth.dto.request.EmailVerificationReqDTO;
import com.project.team4backend.domain.auth.entity.EmailVerification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationConverter {
    public static EmailVerification toEmailVerification(EmailVerificationReqDTO.EmailSendReqDTO emailSendReqDTO, String code) {
        return EmailVerification.builder()
                .email(emailSendReqDTO.email())
                .code(code)
                .createdAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusMinutes(3))
                .isVerified(false)
                .build();
    }
}
