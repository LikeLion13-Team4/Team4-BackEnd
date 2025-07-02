package com.project.team4backend.domain.member.converter;

import com.project.team4backend.domain.member.dto.response.MemberResDTO;
import com.project.team4backend.domain.member.entity.Member;
import lombok.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberConverter {

}public static MemberResDTO.MemberPreviewResDTO toMemberPreviewResDTO(Member member) {
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

