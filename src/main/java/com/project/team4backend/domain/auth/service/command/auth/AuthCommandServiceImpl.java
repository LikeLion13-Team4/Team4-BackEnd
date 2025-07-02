package com.project.team4backend.domain.auth.service.command.auth;

import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.exception.auth.AuthErrorCode;
import com.project.team4backend.domain.auth.exception.auth.AuthException;
import com.project.team4backend.global.security.JwtUtil;
import com.project.team4backend.domain.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthCommandServiceImpl implements AuthCommandService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResDTO.JwtResDTO reissueToken(AuthResDTO.JwtResDTO jwtDto) {

        log.info("[ Auth Service ] 토큰 재발급을 시작합니다.");
        String refreshToken = jwtDto.refreshToken();

        //Access Token 으로부터 사용자 Email 추출
        String email = jwtUtil.getEmail(refreshToken); // **수정부분**
        log.info("[ Auth Service ] Email ---> {}", email);

        String storedToken = refreshTokenService.getRefreshToken(email);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new AuthException(AuthErrorCode._INVALID_TOKEN);
        }
        //Refresh Token 이 유효한지 검사
        jwtUtil.validateToken(refreshToken);

        log.info("[ Auth Service ] Refresh Token 이 유효합니다.");

        return jwtUtil.reissueToken(refreshToken);
    }
}