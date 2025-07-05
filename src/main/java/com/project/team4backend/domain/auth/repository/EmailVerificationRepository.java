package com.project.team4backend.domain.auth.repository;

import com.project.team4backend.domain.auth.entity.EmailVerification;
import com.project.team4backend.domain.auth.entity.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    int deleteByExpireAtBefore(LocalDateTime now);

    Optional<EmailVerification> findTopByEmailAndTypeOrderByCreatedAtDesc(String email, Type type);
}
