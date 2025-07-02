package com.project.team4backend.domain.member.dto.request;

import com.project.team4backend.domain.member.entity.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public class MemberReqDTO {

    @Builder
    public record MemberUpdateReqDTO (
            LocalDate birthday,
            String nickname,
            Gender gender,
            Double height,
            Double weight,
            String imageUrl
    ){
    }
}
