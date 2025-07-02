package com.project.team4backend.global.security.handler;

import com.project.team4backend.global.security.JwtUtil;
import com.project.team4backend.domain.auth.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            String accessToken = jwtUtil.resolveAccessToken(request);

            //블랙리스트 token인 경우
            if (redisTemplate.hasKey("blacklist:" + accessToken)) {
                throw new RuntimeException("로그아웃된 accessToken 입니다.");
            }

            if (accessToken != null) {
                jwtUtil.validateToken(accessToken);
                String email = jwtUtil.getEmail(accessToken);

                long expiration = jwtUtil.getExpiration(accessToken);

                redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
                log.info("[LogoutHandler] Access Token 블랙리스트 등록 완료");

                //Refresh Token Redis에서 삭제
                refreshTokenService.deleteRefreshToken(email);
                log.info("[LogoutHandler] Redis에서 Refresh Token 삭제 완료");
            }

        SecurityContextHolder.clearContext();
    }
}
