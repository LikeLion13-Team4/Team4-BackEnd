package com.project.team4backend.global.security.filter;

import com.project.team4backend.domain.auth.entity.IsTempPassword;
import com.project.team4backend.global.security.CustomUserDetails;
import com.project.team4backend.global.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // JWT 관련 유틸리티 클래스 주입
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate; // 필드 추가 필요

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("[ JwtAuthorizationFilter ] 인가 필터 작동");

        try {
            // 1. Request에서 Access Token 추출
            String accessToken = jwtUtil.resolveAccessToken(request);

            // 2. Access Token이 없으면 다음 필터로 바로 진행
            if (accessToken == null) {
                log.info("[ JwtAuthorizationFilter ] 토큰이 존재하지 않습니다. 다음 필터로 진행.");
                filterChain.doFilter(request, response);
                return;
            }
            //블랙리스트 token이어도 일단 리턴
            if (redisTemplate.hasKey("blacklist:" + accessToken)) {
                log.warn("[ JwtAuthorizationFilter ] 로그아웃된 accessToken 입니다.");
                filterChain.doFilter(request, response);
                return;
            }

            // 3. Access Token을 이용한 인증 처리
            authenticateAccessToken(accessToken);
            log.info("[ JwtAuthorizationFilter ] 종료. 다음 필터로 넘어갑니다.");

        } catch (ExpiredJwtException e) {
            // 4. 토큰 만료 시 401 응답 처리
            logger.warn("[ JwtAuthorizationFilter ] accessToken 이 만료되었습니다.");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Access Token 이 만료되었습니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    // Access Token을 바탕으로 인증 객체 생성 및 SecurityContext에 저장
    private void authenticateAccessToken(String accessToken) {

        log.info("[ JwtAuthorizationFilter ] 토큰으로 인가 과정을 시작합니다. ");

        // 1. Access Token의 유효성 검증
        jwtUtil.validateToken(accessToken);

        log.info("[ JwtAuthorizationFilter ] Access Token 유효성 검증 성공. ");

        // 2. Access Token에서 사용자 정보 추출 후 CustomUserDetails 생성
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRoles(accessToken);
        IsTempPassword isTempPassword = jwtUtil.getIsTempPassword(accessToken);


        CustomUserDetails customUserDetails = new CustomUserDetails(email, null, role, isTempPassword);

        log.info("[ JwtAuthorizationFilter ] UserDetails 객체 생성 성공");

        // 3. 인증 객체 생성 및 SecurityContextHolder에 저장

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority((role)));

        System.out.println("권한 리스트: " + customUserDetails.getAuthorities());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("[ JwtAuthorizationFilter ] 인증 객체 저장 완료");
    }
}