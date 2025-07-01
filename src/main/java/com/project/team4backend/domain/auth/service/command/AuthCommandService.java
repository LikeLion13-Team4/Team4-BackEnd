package com.project.team4backend.domain.auth.service.command;

import com.project.team4backend.domain.auth.dto.response.AuthResDTO;

public interface AuthCommandService {
    AuthResDTO.JwtResDTO reissueToken(AuthResDTO.JwtResDTO jwtDto);
}
