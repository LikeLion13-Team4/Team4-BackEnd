package com.project.team4backend.domain.auth.service.command.auth;

import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.auth.dto.response.AuthResDTO;

public interface AuthCommandService {

    AuthResDTO.SignUpResDTO signUp(AuthReqDTO.SignupReqDTO signupReqDTO);

    AuthResDTO.JwtResDTO reissueToken(AuthResDTO.JwtResDTO jwtDto);
}
