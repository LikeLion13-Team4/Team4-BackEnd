package com.project.team4backend.global.security;

import com.project.team4backend.domain.auth.entity.Auth;
import com.project.team4backend.domain.auth.repository.AuthRepository;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("[ CustomUserDetailsService ] Email을 이용하여 User 검색");
        Optional<Member> userEntity = memberRepository.findByEmail(email);

        if (userEntity.isPresent()) {
            Member member = userEntity.get();
            String role = String.valueOf(member.getRole());

            Optional<Auth> authEntity = authRepository.findByMemberId(member.getId());
            Auth auth = authEntity.get();

            return new CustomUserDetails(member.getId(), member.getEmail(), auth.getPassword(), role, auth.getIsTempPassword());
        }
        throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
    }
}
