package com.project.team4backend.domain.auth.service.command.email;

import com.project.team4backend.domain.auth.converter.EmailVerificationConverter;
import com.project.team4backend.domain.auth.dto.request.EmailVerificationReqDTO;
import com.project.team4backend.domain.auth.entity.EmailVerification;
import com.project.team4backend.domain.auth.exception.email.EmailVerificationErrorCode;
import com.project.team4backend.domain.auth.exception.email.EmailVerificationException;
import com.project.team4backend.domain.auth.repository.EmailVerificationRepository;
import com.project.team4backend.domain.auth.service.EmailTemplateBuilder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class EmailVerificationCommandServiceImpl implements EmailVerificationCommandService  {

    private final JavaMailSender javaMailSender;
    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailVerificationRepository emailVerificationRepository;

    //이메일인증 정보 생성
    @Override
    public String createEmailVerification(EmailVerificationReqDTO.EmailSendReqDTO emailSendReqDTO) {
        String code = generateCode();
        EmailVerification emailVerification = EmailVerificationConverter.toEmailVerification(emailSendReqDTO, code);
        emailVerificationRepository.save(emailVerification);
        return code;
    }
    //이메일 인증 코드 검증
    @Override
    public EmailVerification checkVerificationCode(EmailVerificationReqDTO.EmailVerifyReqDTO emailVerifyReqDTO) {
        EmailVerification emailVerification = emailVerificationRepository
                .findTopByEmailAndTypeOrderByCreatedAtDesc(emailVerifyReqDTO.email(), emailVerifyReqDTO.type())
                .orElseThrow(() -> new EmailVerificationException(EmailVerificationErrorCode._NOT_FOUND));

        // 인증 완료 여부 판단 - 이메일이 동일해서 가져왔는데 인증 되어있을 수 있기 때문이다.
        if (emailVerification.getIsVerified()) {
            throw new EmailVerificationException(EmailVerificationErrorCode._ALREADY_VERIFIED);
        }

        // 해당 코드 만료 여부 판단
        if (emailVerification.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new EmailVerificationException(EmailVerificationErrorCode._EXPIRED);
        }

        // 해당 코드 일치 여부 판단
        if (!emailVerification.getCode().equals(emailVerifyReqDTO.authCode())) {
            throw new EmailVerificationException(EmailVerificationErrorCode._BAD_REQUEST);
        }
        return emailVerification;
    }
    // 이메일 인증 코드 검증 후 최종적으로 isVerified = true 처리 로직
    @Override
    public void emailVerificationAndMark(EmailVerificationReqDTO.EmailVerifyReqDTO emailVerifyReqDTO) {
        EmailVerification emailVerification = checkVerificationCode(emailVerifyReqDTO);
        emailVerification.markAsVerified();
    }
    //이메일 전송 전용 메서드
    @Override
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();  // MIME 형식의 이메일 객체 생성
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");  // 도우미 클래스: 텍스트 설정 쉽게

            helper.setTo(to);               // 수신자 설정
            helper.setSubject(subject);     // 메일 제목 설정
            helper.setFrom("tjgustjr16@naver.com");  // 발신자 설정 (SMTP 계정과 동일해야 함)
            helper.setText(htmlBody, true); // 본문 설정 (true → HTML 형식)

            javaMailSender.send(message);       // 메일 전송
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);  // 예외 발생 시 사용자 정의 예외로 감싸기
        }
    }

    // 임시 비번 이메일로 전송
    @Override
    public void sendTempPassword(String email) {
        String html = emailTemplateBuilder.buildTempPasswordHtml(generateTempPassword());
        sendHtmlEmail(email, "인증번호 안내", html);
    }

    // 인증 코드 이메일로 전송
    @Override
    public void sendVerificationCode(String email, String code) {
        String html = emailTemplateBuilder.buildVerifyEmailHtml(code);
        sendHtmlEmail(email, "인증 코드 전송", html);
    }

    // 인증 코드 생성
    private String generateCode() {
        Random random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }
    //임시 비밀번호 발급용 메서드
    private String generateTempPassword() {
        return new SecureRandom().ints(10, 33, 122)
                .mapToObj(i -> (char) i)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}

