package com.project.team4backend.domain.auth.converter;

import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.entity.Auth;
import com.project.team4backend.domain.auth.entity.enums.IsTempPassword;
import com.project.team4backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConverter {
    public static Auth toAuth(Member member, String encodedPassword) {
        return Auth.builder()
                .password(encodedPassword)
                .isTempPassword(IsTempPassword.NORMAL)
                .member(member)
                .build();
    }

    public static AuthResDTO.SignUpResDTO toSignUpResDTO(Member member) {
        return AuthResDTO.SignUpResDTO.builder()
                .id(member.getId())
                .createAt(LocalDateTime.now())
                .build();
    }
}
