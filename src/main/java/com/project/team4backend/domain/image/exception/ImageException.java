package com.project.team4backend.domain.image.exception;


import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class ImageException extends CustomException {
    public ImageException(ImageErrorCode errorCode) {
        super(errorCode);
    }
}