package com.project.team4backend.domain.auth.service.scheduler;

import com.project.team4backend.domain.auth.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailVerificationScheduler {

    private final EmailVerificationRepository emailVerificationRepository;

    //1시간 단위로 EmailVerification DB 정리 - 만료된 정보들만
    @Scheduled(cron = "0 0 */1 * * *")
    public void cleanExpiredEmailVerifications() {
        log.info("Cleaning expired email verifications");

        int deletedCount = emailVerificationRepository.deleteByExpireAtBefore(LocalDateTime.now());

        if (deletedCount > 0) {
            log.info("만료된 이메일 인증 정보 {}개가 삭제 되었습니다.", deletedCount);
        }

    }
}
