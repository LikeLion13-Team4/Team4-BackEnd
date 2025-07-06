package com.project.team4backend.domain.member.dto.request;

import com.project.team4backend.domain.member.entity.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

public class MemberReqDTO {
    @Builder
    public record MemberAccountUpdateReqDTO (
            String nickname
    ){
    }

    @Builder
    public record MemberBodyUpdateReqDTO (
            LocalDate birthday,
            Gender gender,
            Double height,
            Double weight
    ){
    }
}
