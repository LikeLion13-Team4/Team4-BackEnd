package com.project.team4backend.domain.member.service.command;

import com.project.team4backend.domain.auth.entity.Auth;
import com.project.team4backend.domain.auth.exception.auth.AuthErrorCode;
import com.project.team4backend.domain.auth.exception.auth.AuthException;
import com.project.team4backend.domain.auth.repository.AuthRepository;
import com.project.team4backend.domain.member.dto.request.MemberReqDTO;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
import com.project.team4backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    //프로필 이미지 업로드1(이미지 선택)
    @Override
    public void selectProfileImage(String email, String fileKey){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.selectImage(fileKey);
    }

    // 계정 정보 수정
    @Override
    public String updateMemberAccount(String email, MemberReqDTO.MemberAccountUpdateReqDTO memberAccountUpdateReqDTO) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        boolean changed = member.updateAccountInfo(memberAccountUpdateReqDTO.nickname());
        return changed ? "계정정보가 변경되었습니다." : "변경된 정보가 없습니다.";
    }

    // 신체 정보 수정
    @Override
    public String updateMemberBody(String email, MemberReqDTO.MemberBodyUpdateReqDTO memberBodyUpdateReqDTO) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        boolean changed = member.updateBodyInfo(memberBodyUpdateReqDTO.birthday(), memberBodyUpdateReqDTO.gender(), memberBodyUpdateReqDTO.height(), memberBodyUpdateReqDTO.weight());
        return changed ? "신체정보가 변경되었습니다." : "변경된 정보가 없습니다.";
    }

    //회원 탈퇴
    @Override
    public void deleteMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Auth auth = authRepository.findByMemberId(member.getId())
                .orElseThrow(()-> new AuthException(AuthErrorCode.AUTH_NOT_FOUND));

        member.deleteMember();
        auth.deleteAuth();
    }
}
