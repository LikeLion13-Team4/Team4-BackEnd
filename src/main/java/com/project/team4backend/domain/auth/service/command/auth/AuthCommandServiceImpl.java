package com.project.team4backend.domain.auth.service.command.auth;

import com.project.team4backend.domain.auth.converter.AuthConverter;
import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.entity.Auth;
import com.project.team4backend.domain.auth.exception.auth.AuthErrorCode;
import com.project.team4backend.domain.auth.exception.auth.AuthException;
import com.project.team4backend.domain.auth.repository.AuthRepository;
import com.project.team4backend.domain.member.converter.MemberConverter;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.global.apiPayload.exception.CustomException;
import com.project.team4backend.global.security.Config.SecurityConfig;
import com.project.team4backend.global.security.JwtUtil;
import com.project.team4backend.domain.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final SecurityConfig securityConfig;
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    @Override
    public AuthResDTO.SignUpResDTO signUp(AuthReqDTO.SignupReqDTO signupReqDTO) {
        // Member 생성
        Member member = MemberConverter.toMember(signupReqDTO);

        // Member 엔티티 DB에 저장
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(MemberErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        String password = signupReqDTO.password();
        String encodedPassword = null;
        if (password != null) {
            encodedPassword = securityConfig.passwordEncoder().encode(password);
        }
        // Auth 생성
        Auth auth =  AuthConverter.toAuth(member, encodedPassword);
        // Auth 엔티티 저장
        authRepository.save(auth);
        // 응답 DTO로 변환 후 return
        return AuthConverter.toSignUpResDTO(member);
    }

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