package com.project.team4backend.domain.member.service.command;

import com.project.team4backend.domain.image.dto.request.ImageReqDTO;
import com.project.team4backend.domain.image.dto.response.ImageResDTO;
import com.project.team4backend.domain.member.dto.request.MemberReqDTO;
import com.project.team4backend.domain.member.entity.Member;

public interface MemberCommandService {
    void selectProfileImage(String email, String fileKey);

    ImageResDTO.SaveImageResDTO saveProfileImage(Member member, String fileKey, String ImageUrl, ImageReqDTO.SaveImageReqDTO saveImageReqDTO);

    String updateMemberAccount(String email, MemberReqDTO.MemberAccountUpdateReqDTO memberAccountUpdateReqDTO);

    String updateMemberBody(String email, MemberReqDTO.MemberBodyUpdateReqDTO memberBodyUpdateReqDTO);

    void deleteMember(String email);

    void deleteProfileImage(String email);
}
