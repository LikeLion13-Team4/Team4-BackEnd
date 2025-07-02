package com.project.team4backend.domain.auth.exception.email;

import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class EmailVerificationException extends CustomException {
    public EmailVerificationException(EmailVerificationErrorCode errorCode) {
        super(errorCode);
    }
}