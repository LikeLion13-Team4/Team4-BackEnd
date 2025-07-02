package com.project.team4backend.domain.member.contoller;

import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.member.service.query.MemberQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Tag(name="Member", description = "Member 관련 API")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private MemberRepository memberRepository;


//    @Operation(description = "내 정보 조회")
//    @GetMapping()
//    public CustomResponse<MemberResDTO.MemberPreviewResDTO> getMember(
//            @Auth
//    ) {
//        return CustomResponse.onSuccess(memberQueryService.getMember(customUserDetails.getUsername()));
//    }
}
