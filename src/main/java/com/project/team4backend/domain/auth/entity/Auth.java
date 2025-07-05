package com.project.team4backend.domain.auth.entity;

import com.project.team4backend.domain.auth.entity.enums.IsTempPassword;
import com.project.team4backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "is_temp")
    @Enumerated(EnumType.STRING)
    private IsTempPassword isTempPassword;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateIsTempPassword(IsTempPassword isTempPassword) {
        this.isTempPassword = isTempPassword;
    }

}
