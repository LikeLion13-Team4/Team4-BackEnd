package com.project.team4backend.domain.auth.service.command.auth;

import com.project.team4backend.domain.auth.converter.AuthConverter;
import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.entity.Auth;
import com.project.team4backend.domain.auth.entity.EmailVerification;
import com.project.team4backend.domain.auth.entity.enums.IsTempPassword;
import com.project.team4backend.domain.auth.exception.auth.AuthErrorCode;
import com.project.team4backend.domain.auth.exception.auth.AuthException;
import com.project.team4backend.domain.auth.repository.AuthRepository;
import com.project.team4backend.domain.auth.service.command.email.EmailVerificationCommandService;
import com.project.team4backend.domain.member.converter.MemberConverter;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
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

import java.security.SecureRandom;
import java.util.stream.Collectors;

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
    private final EmailVerificationCommandService emailVerificationCommandService;

    @Override
    public AuthResDTO.SignUpResDTO signUp(AuthReqDTO.SignupReqDTO signupReqDTO, EmailVerification emailVerification) {
        if (memberRepository.existsByEmailAndIsDeletedFalse(signupReqDTO.email())) {
            throw new CustomException(MemberErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // Member 생성
        Member member = MemberConverter.toMember(signupReqDTO);

        // Member 저장
        memberRepository.save(member);

        String password = signupReqDTO.password();
        String encodedPassword = null;
        if (password != null) {
            encodedPassword = securityConfig.passwordEncoder().encode(password);
        }
        // Auth 생성
        Auth auth =  AuthConverter.toAuth(member, encodedPassword);
        // Auth 엔티티 저장
        authRepository.save(auth);

        // SIGNUP 타입의 emailVerfication 정보의 isVerified = true 설정 -> 이메일 인증 성공 최종 승인
        emailVerification.markAsVerified();

        // 응답 DTO로 변환 후 return
        return AuthConverter.toSignUpResDTO(member);
    }
    @Override
    public void updatePassword(AuthReqDTO.UpdatePasswordReqDTO updatePasswordReqDTO, EmailVerification emailVerification) {
        Member member = memberRepository.findByEmail(emailVerification.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Auth auth = authRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.AUTH_NOT_FOUND));

        // 현재 비밀번호가 일치하지 않는 경우
        if (!securityConfig.passwordEncoder().matches(updatePasswordReqDTO.currentPassword(), auth.getPassword())) {
            throw new AuthException(AuthErrorCode.AUTH_WRONG_PASSWORD);
        }
        // 새 비밀번호가 현재 비밀번호와 같은 경우
        if (updatePasswordReqDTO.newPassword().equals(updatePasswordReqDTO.currentPassword())) {
            throw new AuthException(AuthErrorCode.AUTH_SAME_PASSWORD);
        }
        // 새 비밀번호 암호화 후 업데이트
        String encodedNewPassword = securityConfig.passwordEncoder().encode(updatePasswordReqDTO.newPassword());
        auth.updatePassword(encodedNewPassword);

        //만약 해당 계정이 임시 비번을 발급 받은 상황이었다면
        if (auth.getIsTempPassword() == IsTempPassword.IS_TEMP_PASSWORD) {
            auth.updateIsTempPassword(IsTempPassword.NORMAL); // 이제 임시 비밀번호가 아님을 알 수 있다.
        }

        // CHANGE_PASSWORD 타입의 emailVerfication 정보의 isVerified = true 설정 -> 이메일 인증 성공 최종 승인
        emailVerification.markAsVerified();
    }
    @Override
    public void updateTempPassword(AuthReqDTO.TempPasswordReqDTO tempPasswordReqDTO, EmailVerification emailVerification) {
        Member member = memberRepository.findByEmail(emailVerification.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Auth auth = authRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new AuthException(AuthErrorCode.AUTH_NOT_FOUND));

        String tempPassword = generateTempPassword();
        String encodedPassword = null;
        if (tempPassword != null) {
            encodedPassword = securityConfig.passwordEncoder().encode(tempPassword);
            auth.updatePassword(encodedPassword);
        }
        //저장된 임시 비밀번호 전송
        emailVerificationCommandService.sendTempPassword(emailVerification.getEmail(), tempPassword);
        //임시 비번임을 설정
        auth.updateIsTempPassword(IsTempPassword.IS_TEMP_PASSWORD);
        // TEMP_PASSWORD 타입의 emailVerfication 정보의 isVerified = true 설정 -> 이메일 인증 성공 최종 승인
        emailVerification.markAsVerified();
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
            throw new AuthException(AuthErrorCode.AUTH_INVALID_TOKEN);
        }
        //Refresh Token 이 유효한지 검사
        jwtUtil.validateToken(refreshToken);

        log.info("[ Auth Service ] Refresh Token 이 유효합니다.");

        return jwtUtil.reissueToken(refreshToken);
    }
    //임시 비밀번호 발급용 메서드
    private String generateTempPassword() {
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        return random.ints(10, 0, charSet.length())
                .mapToObj(charSet::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}