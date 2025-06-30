package com.project.team4backend.domain.auth.exception;

import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class AuthException extends CustomException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}