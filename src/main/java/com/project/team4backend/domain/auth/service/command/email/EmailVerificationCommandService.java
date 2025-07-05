package com.project.team4backend.domain.auth.service.command.email;

import com.project.team4backend.domain.auth.dto.request.EmailVerificationReqDTO;
import com.project.team4backend.domain.auth.entity.EmailVerification;

public interface EmailVerificationCommandService {
    String createEmailVerification(EmailVerificationReqDTO.EmailSendReqDTO emailSendReqDTO);

    EmailVerification checkVerificationCode(EmailVerificationReqDTO.EmailVerifyReqDTO emailVerifyReqDTO);

    void emailVerificationAndMark(EmailVerificationReqDTO.EmailVerifyReqDTO emailVerifyReqDTO);

    void sendHtmlEmail(String to, String subject, String htmlContent);

    void sendTempPassword(String email);

    void sendVerificationCode(String email, String code);
}
