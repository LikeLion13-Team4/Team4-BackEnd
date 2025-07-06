package com.project.team4backend.domain.member.contoller;

import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.member.service.query.MemberQueryService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Tag(name="Member", description = "Member 관련 API")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private MemberRepository memberRepository;

    @Operation(method = "GET", summary = "회원 정보 조회", description = "회원 정보 조회 api입니다.")
    @GetMapping
    public CustomResponse<MemberResDTO.MemberPreviewResDTO> getMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return CustomResponse.onSuccess(memberQueryService.getMemberPreview(customUserDetails.getEmail()));
    }

//    @Operation(description = "내 정보 조회")
//    @GetMapping()
//    public CustomResponse<MemberResDTO.MemberPreviewResDTO> getMember(
//            @Auth
//    ) {
//        return CustomResponse.onSuccess(memberQueryService.getMember(customUserDetails.getUsername()));
//    }
}
