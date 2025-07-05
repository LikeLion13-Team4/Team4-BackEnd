package com.project.team4backend.domain.auth.converter;

import com.project.team4backend.domain.auth.dto.request.EmailVerificationReqDTO;
import com.project.team4backend.domain.auth.entity.EmailVerification;
import com.project.team4backend.domain.auth.entity.enums.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationConverter {
    public static EmailVerification toEmailVerification(EmailVerificationReqDTO.EmailSendReqDTO emailSendReqDTO, String message) {
        return EmailVerification.builder()
                .email(emailSendReqDTO.email())
                .message(message)
                .type(emailSendReqDTO.type())
                .createdAt(LocalDateTime.now())
                .expireAt(LocalDateTime.now().plusMinutes(3))
                .isVerified(false)
                .build();
    }

    public static EmailVerificationReqDTO.EmailVerifyReqDTO toEmailVerifyReqDTO(String email, String authCode, Type type) {
        return EmailVerificationReqDTO.EmailVerifyReqDTO.builder()
                .email(email)
                .authCode(authCode)
                .type(type)
                .build();
    }
}
