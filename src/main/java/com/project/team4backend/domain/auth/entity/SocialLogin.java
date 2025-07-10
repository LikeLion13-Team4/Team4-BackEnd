package com.project.team4backend.domain.auth.entity;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "social_login",
        uniqueConstraints = @UniqueConstraint(columnNames = {"social_type", "social_id"}) // 소셜이 다르면 social_id가 겹칠 수 있음
)
public class SocialLogin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_auth_id")
    private Long socialAuthId;

    @Column(name = "social_type")
    private String socialType;

    @Column(name = "social_id")
    private String socialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
