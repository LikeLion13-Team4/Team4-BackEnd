package com.project.team4backend.domain.member.contoller;

import com.project.team4backend.domain.member.dto.request.MemberReqDTO;
import com.project.team4backend.domain.member.dto.response.MemberResDTO;
import com.project.team4backend.domain.member.service.command.MemberCommandService;
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
    private final MemberCommandService memberCommandService;

    @Operation(method = "PATCH", summary = "계정 정보 수정", description = "회원 정보 수정 api입니다.")
    @PatchMapping("/body")
    public CustomResponse<String> updateMemberAccount(
            @RequestBody MemberReqDTO.MemberAccountUpdateReqDTO memberAccountUpdateReqDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return CustomResponse.onSuccess(memberCommandService.updateMemberAccount(customUserDetails.getEmail(), memberAccountUpdateReqDTO));
    }

    @Operation(method = "PATCH", summary = "신체 정보 수정", description = "신체 정보 수정 api입니다.")
    @PatchMapping("/account")
    public CustomResponse<String> updateMember(
            @RequestBody MemberReqDTO.MemberBodyUpdateReqDTO memberBodyUpdateReqDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return CustomResponse.onSuccess(memberCommandService.updateMemberBody(customUserDetails.getEmail(), memberBodyUpdateReqDTO));
    }


    @Operation(method = "GET", summary = "회원 정보 조회", description = "회원 정보 조회 api입니다.")
    @GetMapping
    public CustomResponse<MemberResDTO.MemberPreviewResDTO> getMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return CustomResponse.onSuccess(memberQueryService.getMemberPreview(customUserDetails.getEmail()));
    }

    @Operation(method = "DELETE", summary = "회원 탈퇴", description = "회원 탈퇴 작동 시 해당 유저의 is_deleted = true로 바뀜")
    @DeleteMapping
    public CustomResponse<String> deleteMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        memberCommandService.deleteMember(customUserDetails.getEmail());
        return CustomResponse.onSuccess("회원정보가 삭제되었습니다.");
    }

}
