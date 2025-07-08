package com.project.team4backend.domain.member.service.command;

import com.project.team4backend.domain.image.dto.response.ImageResDTO;
import com.project.team4backend.domain.member.dto.request.MemberReqDTO;

public interface MemberCommandService {
    ImageResDTO.SaveImageResDTO uploadProfileImage(String email, String fileKey, String imageUrl);

    String updateMemberAccount(String email, MemberReqDTO.MemberAccountUpdateReqDTO memberAccountUpdateReqDTO);

    String updateMemberBody(String email, MemberReqDTO.MemberBodyUpdateReqDTO memberBodyUpdateReqDTO);

    void deleteMember(String email);

    void deleteProfileImage(String email);
}
