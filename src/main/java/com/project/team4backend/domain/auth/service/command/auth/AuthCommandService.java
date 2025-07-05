package com.project.team4backend.domain.auth.service.command.auth;

import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.entity.EmailVerification;

public interface AuthCommandService {

    //회원가입
    AuthResDTO.SignUpResDTO signUp(AuthReqDTO.SignupReqDTO signupReqDTO, EmailVerification emailVerification);

    //비밀번호 변경
    void updatePassword(AuthReqDTO.UpdatePasswordReqDTO updatePasswordReqDTO, EmailVerification emailVerification);

    //토큰 재발급
    AuthResDTO.JwtResDTO reissueToken(AuthResDTO.JwtResDTO jwtDto);
}
