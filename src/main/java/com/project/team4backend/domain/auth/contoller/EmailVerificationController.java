package com.project.team4backend.domain.auth.contoller;

import com.project.team4backend.domain.auth.dto.request.EmailVerificationReqDTO;
import com.project.team4backend.domain.auth.service.command.email.EmailVerificationCommandService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "EmailVerification 관련 api", description = "EmailVerification 관련 API입니다.")
@RequestMapping("/api/v1/auths/emailVerifications")
public class EmailVerificationController {

    private final EmailVerificationCommandService emailVerificationCommandService;

    @Operation(method = "POST", summary = "이메일 인증 코드 전송", description = "이메일 인증용 코드 전송과 이메일전송 정보를 db에 저장")
    @PostMapping("/send")
    public CustomResponse<String> SendEmailVerificationCode(@RequestBody EmailVerificationReqDTO.EmailSendReqDTO emailSendReqDTO) {
        String code = emailVerificationCommandService.createEmailVerification(emailSendReqDTO); // 이메일인증 정보 생성
        emailVerificationCommandService.sendVerificationCode(emailSendReqDTO.email(), code); // 인증 코드 전송
        return CustomResponse.onSuccess("이메일 인증 코드가 전송 되었습니다.");
    }

    @Operation(method = "POST", summary = "이메일 인증 코드 검증", description = "이메일 인증용 코드를 db에 저장된 정보와 비교")
    @PostMapping("/check")
    public CustomResponse<String> CheckEmailVerificationCode(@RequestBody EmailVerificationReqDTO.EmailVerifyReqDTO emailVerifyReqDTO) {
        emailVerificationCommandService.checkVerificationCode(emailVerifyReqDTO);
        return CustomResponse.onSuccess("이메일 인증 완료 되었습니다.");
    }
}
