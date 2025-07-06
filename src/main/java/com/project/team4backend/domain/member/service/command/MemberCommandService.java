package com.project.team4backend.domain.member.service.command;

import com.project.team4backend.domain.member.dto.request.MemberReqDTO;

public interface MemberCommandService {
    String updateMemberAccount(String email, MemberReqDTO.MemberAccountUpdateReqDTO memberAccountUpdateReqDTO);

    String updateMemberBody(String email, MemberReqDTO.MemberBodyUpdateReqDTO memberBodyUpdateReqDTO);

    void deleteMember(String email);
}
