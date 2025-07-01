package com.project.team4backend.domain.auth.dto.request;

import lombok.Builder;

public class AuthReqDTO {
    @Builder
    public record LoginReqDTO(
            String email,
            String password
    ) {
    }
}
