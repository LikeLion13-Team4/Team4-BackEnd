package com.project.team4backend.domain.member.converter;

import com.project.team4backend.domain.auth.dto.request.AuthReqDTO;
import com.project.team4backend.domain.member.dto.response.MemberResDTO;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.entity.enums.Role;
import lombok.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {
    public static Member toMember(AuthReqDTO.SignupReqDTO signupReqDTO) {
        return Member.builder()
                .nickname(signupReqDTO.nickname())
                .email(signupReqDTO.email())
                .birthday(signupReqDTO.birthday())
                .height(signupReqDTO.height())
                .weight(signupReqDTO.weight())
                .gender(signupReqDTO.gender())
                .profileImageUrl(null)
                .role(Role.ROLE_USER)
                .build();
    }
    public static MemberResDTO.MemberPreviewResDTO toMemberPreviewResDTO(Member member) {
        return MemberResDTO.MemberPreviewResDTO.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .birthday(member.getBirthday())
                .gender(member.getGender())
                .height(member.getHeight())
                .weight(member.getWeight())
                .imageUrl(member.getProfileImageUrl())
                .build();
    }

}

