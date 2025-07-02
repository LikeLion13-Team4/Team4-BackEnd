package com.project.team4backend.domain.auth.dto.request;

import com.project.team4backend.domain.member.entity.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public class AuthReqDTO {
    @Builder
    public record SignupReqDTO(
            String email,
            String password,
            LocalDate birthday,
            String nickname,
            Gender gender,        // Enum (예: MALE, FEMALE)
            Double height,
            Double weight,
            String authCode
    ) {
    }
    @Builder
    public record LoginReqDTO(
            String email,
            String password
    ) {
    }
}
