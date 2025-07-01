package com.project.team4backend.domain.auth.entity;

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

    private String email;
    private String code;

    @Enumerated(EnumType.STRING)
    private  Type type;

    private LocalDateTime createAt;
    private LocalDateTime expireAt;
    private Boolean isVerified;
}
