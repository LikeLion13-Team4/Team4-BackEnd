package com.project.team4backend.domain.auth.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

public class AuthResDTO {
    @Builder
    public record JwtResDTO(
            String accessToken,
            String refreshToken
    ){}

    @Builder
    public record SignUpResDTO(
            Long id,
            LocalDateTime createAt
    ){
    }

    @Builder
    public record LoginResDTO(
            String email,
            String accessToken,
            String refreshToken
    ){}
}
