package com.project.team4backend.domain.member.service.query;

import com.project.team4backend.domain.member.dto.response.MemberResDTO;

public interface MemberQueryService {
    MemberResDTO.MemberPreviewResDTO getMemberPreview(String email);
}
