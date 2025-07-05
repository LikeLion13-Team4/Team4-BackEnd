package com.project.team4backend.domain.auth.contoller;

import com.project.team4backend.domain.auth.converter.EmailVerificationConverter;
import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.entity.EmailVerification;
import com.project.team4backend.domain.auth.entity.enums.Type;
import com.project.team4backend.domain.auth.service.command.auth.AuthCommandService;
import com.project.team4backend.domain.auth.service.command.email.EmailVerificationCommandService;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
@Tag(name = "Auth API", description = "Auth 관련 API입니다.")
public class AuthController {

    private final AuthCommandService authCommandService;
    private final EmailVerificationCommandService emailVerificationCommandService;

    private final MemberRepository memberRepository;


    @Operation(method = "POST", summary = "회원가입", description = "email과 password 입력하여 회원가입")
    @PostMapping("/signup")
    public CustomResponse<AuthResDTO.SignUpResDTO> signIn(@RequestBody AuthReqDTO.SignupReqDTO signupReqDTO) {
        //이메일 인증 코드 검증
        EmailVerification emailVerification = emailVerificationCommandService.checkVerificationCode(EmailVerificationConverter.toEmailVerifyReqDTO(signupReqDTO.email(), signupReqDTO.authCode(), Type.SIGNUP));
        return CustomResponse.onSuccess(authCommandService.signUp(signupReqDTO,emailVerification));
    }

    @Operation(method = "POST", summary = "로그인", description = "jwt 발급은 필터에서 처리된다.")
    @PostMapping("/login")
    public void login(@RequestBody AuthReqDTO.LoginReqDTO loginReqDTO) {
    }

    @Operation(method = "POST", summary = "로그아웃", description = "security handler에서 처리")
    @PostMapping("/logout")
    public void logout() {
    }

    @Operation(method = "POST", summary ="비밀번호 변경", description = "비밀번호 변경 api인데, 정확히는 비밀번호를 입력해서 수정이 아닌 새로 생성해서 덮어쓰기")
    @PostMapping("/reset-password")
    public CustomResponse<String> resetUpdatePassword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody AuthReqDTO.UpdatePasswordReqDTO updatePasswordReqDTO){
        //이메일 인증 코드 검증
        EmailVerification emailVerification = emailVerificationCommandService.checkVerificationCode(EmailVerificationConverter.toEmailVerifyReqDTO(customUserDetails.getEmail(), updatePasswordReqDTO.authCode(), Type.CHANGE_PASSWORD));
        authCommandService.updatePassword(updatePasswordReqDTO, emailVerification);
        return CustomResponse.onSuccess("비밀번호가 변경 되었습니다.");
    }

    @Operation(method = "POST", summary = "임시 비밀번호 발급", description = "이메일 인증을 통해 임시 비밀번호를 이메일로 전송")
    @PostMapping("/reset-temp-password")
    public CustomResponse<String> resetTempPassword(@RequestBody AuthReqDTO.TempPasswordReqDTO tempPasswordReqDTO){
        //이메일 인증 코드 검증
        EmailVerification emailVerification = emailVerificationCommandService.checkVerificationCode(EmailVerificationConverter.toEmailVerifyReqDTO(tempPasswordReqDTO.email(), tempPasswordReqDTO.authCode(), Type.TEMP_PASSWORD));
        authCommandService.updateTempPassword(tempPasswordReqDTO, emailVerification);
        return CustomResponse.onSuccess("임시 비밀번호가 전송되었습니다.");
    }

    //토큰 재발급 API
    @Operation(method = "POST", summary = "토큰 재발급", description = "토큰 재발급. accessToken과 refreshToken을 body에 담아서 전송합니다.")
    @PostMapping("/reissue")
    public CustomResponse<?> reissue(@RequestBody AuthResDTO.JwtResDTO jwtResDTO) throws SignatureException {

        log.info("[ Auth Controller ] 토큰을 재발급합니다. ");

        return CustomResponse.onSuccess(authCommandService.reissueToken(jwtResDTO));
    }
}