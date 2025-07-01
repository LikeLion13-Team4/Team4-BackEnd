package com.project.team4backend.domain.auth.dto.response;

import lombok.Builder;

public class AuthResDTO {
    @Builder
    public record JwtResDTO(
            String accessToken,
            String refreshToken
    ){}

    @Builder
    public record LoginResDTO(
            String nickname,
            String accessToken,
            String refreshToken
    ){}
}
