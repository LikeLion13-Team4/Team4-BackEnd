package com.project.team4backend.domain.auth.contoller;

import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.auth.dto.request.EmailVerificationReqDTO;
import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.service.command.auth.AuthCommandService;
import com.project.team4backend.domain.auth.service.command.email.EmailVerificationCommandService;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SignatureException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
@Tag(name = "Auth 관련 api", description = "Auth 관련 API입니다.")
public class AuthController {

    private final AuthCommandService authCommandService;
    private final EmailVerificationCommandService emailVerificationCommandService;

    private final MemberRepository memberRepository;


    @Operation(method = "POST", summary = "회원가입", description = "email과 password 입력하여 회원가입")
    @PostMapping("/signup")
    public CustomResponse<AuthResDTO.SignUpResDTO> signIn(@RequestBody AuthReqDTO.SignupReqDTO signupReqDTO) {
        //이메일 인증 코드 검증
        emailVerificationCommandService.checkVerificationCode(
                new EmailVerificationReqDTO.EmailVerifyReqDTO(
                        signupReqDTO.email(),
                        signupReqDTO.authCode()
                )
        );
        return CustomResponse.onSuccess(authCommandService.signUp(signupReqDTO));
    }

    @Operation(method = "POST", summary = "로그인", description = "jwt 발급은 필터에서 처리된다.")
    @PostMapping("/login")
    public void login(@RequestBody AuthReqDTO.LoginReqDTO loginReqDTO) {
    }

    //토큰 재발급 API
    @Operation(method = "POST", summary = "토큰 재발급", description = "토큰 재발급. accessToken과 refreshToken을 body에 담아서 전송합니다.")
    @PostMapping("/reissue")
    public CustomResponse<?> reissue(@RequestBody AuthResDTO.JwtResDTO jwtResDTO) throws SignatureException {

        log.info("[ Auth Controller ] 토큰을 재발급합니다. ");

        return CustomResponse.onSuccess(authCommandService.reissueToken(jwtResDTO));
    }
}