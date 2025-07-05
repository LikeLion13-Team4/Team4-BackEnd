package com.project.team4backend.domain.auth.entity;

import com.project.team4backend.domain.auth.entity.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailVerificationId;

    @Column(name = "email")
    private String email;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @Column(name = "is_verified")
    private Boolean isVerified;

    public void markAsVerified() {
        this.isVerified = true;
    }
}
