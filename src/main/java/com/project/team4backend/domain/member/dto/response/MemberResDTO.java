package com.project.team4backend.domain.member.dto.response;

import com.project.team4backend.domain.member.entity.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public class MemberResDTO {
    @Builder
    public record MemberPreviewResDTO(
            Long userId,
            String email,
            LocalDate birthday,
            String nickname,
            Gender gender,
            Double height,
            Double weight,
            String imageUrl
    ) {
    }
}
