package com.project.team4backend.domain.member.contoller;

import com.project.team4backend.domain.image.dto.request.ImageReqDTO;
import com.project.team4backend.domain.image.dto.response.ImageResDTO;
import com.project.team4backend.domain.image.service.command.ImageCommandService;
import com.project.team4backend.domain.member.dto.request.MemberReqDTO;
import com.project.team4backend.domain.member.dto.response.MemberResDTO;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
import com.project.team4backend.domain.member.repository.MemberRepository;
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
    private final ImageCommandService imageCommandService;
    private final MemberRepository memberRepository;

    @Operation(method = "POST", summary = "프로필 이미지 업로드1", description = "프로필 이미지 선택 api, 업로드 하는건 아님")
    @PostMapping("/profile-image1")
    public CustomResponse<ImageResDTO.PresignedUrlResDTO> uploadProfileImages
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestBody ImageReqDTO.PresignedUrlDTO presignedUrlDTO) {
        ImageResDTO.PresignedUrlResDTO presignedUrlResDTO = imageCommandService.generatePresignedUrl(presignedUrlDTO); // presignedUrl 발급

        memberCommandService.selectProfileImage(customUserDetails.getEmail(), presignedUrlResDTO.fileKey()); // member의 profileImageKey에 fileKey 저장
        return CustomResponse.onSuccess(presignedUrlResDTO);
    }

    @Operation(method = "POST", summary = "프로필 이미지 업로드2", description = "선택한 프로필 이미지 저장 api, 이때 업로드")
    @PostMapping("/profile-image2")
    public CustomResponse<ImageResDTO.SaveImageResDTO> saveProfileImages(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ImageReqDTO.SaveImageReqDTO saveImageReqDTO) {

        Member member = memberRepository.findByEmail(customUserDetails.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String fileKey = member.getProfileImageKey();
        return CustomResponse.onSuccess(memberCommandService.saveProfileImage(member, fileKey, imageCommandService.commit(fileKey), saveImageReqDTO));
    }

    @Operation(method = "GET", summary = "회원 정보 조회", description = "회원 정보 조회 api입니다.")
    @GetMapping
    public CustomResponse<MemberResDTO.MemberPreviewResDTO> getMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return CustomResponse.onSuccess(memberQueryService.getMemberPreview(customUserDetails.getEmail()));
    }

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

    @Operation(method = "DELETE", summary = "회원 탈퇴", description = "회원 탈퇴 작동 시 해당 유저의 is_deleted = true로 바뀜")
    @DeleteMapping
    public CustomResponse<String> deleteMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        memberCommandService.deleteMember(customUserDetails.getEmail());
        return CustomResponse.onSuccess("회원정보가 삭제되었습니다.");
    }

}
