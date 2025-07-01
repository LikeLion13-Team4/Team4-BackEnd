package com.project.team4backend.global.security;

import com.project.team4backend.domain.auth.dto.response.AuthResDTO;
import com.project.team4backend.domain.auth.entity.enums.IsTempPassword;
import com.project.team4backend.domain.auth.service.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessExpMs;
    private final Long refreshExpMs;
    private final RefreshTokenService refreshTokenService;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.token.access-expiration-time}") Long access,
            @Value("${spring.jwt.token.refresh-expiration-time}") Long refresh,
            RefreshTokenService refreshTokenService
    ) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        accessExpMs = access;
        refreshExpMs = refresh;
        this.refreshTokenService = refreshTokenService;

    }

    public String getEmail(String token) throws SignatureException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // JWT 토큰을 입력으로 받아 토큰의 claim 에서 사용자 권한을 추출하는 메서드
    public String getRoles(String token) throws SignatureException{
        log.info("[ JwtUtil ] 토큰에서 권한을 추출합니다.");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public IsTempPassword getIsTempPassword(String token) throws SignatureException {
        String tempPwdStr = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("isTempPassword", String.class);
        return IsTempPassword.valueOf(tempPwdStr);
    }

    // Token 발급하는 메서드
    public String tokenProvider(CustomUserDetails customUserDetails, Instant expiration) {

        log.info("[ JwtUtil ] 토큰을 새로 생성합니다.");
        //현재 시간
        Instant issuedAt = Instant.now();

        return Jwts.builder()
                .header() //헤더 부분
                .add("typ", "JWT") // JWT type
                .and()
                .subject(customUserDetails.getUsername()) //Subject 에 username (email) 추가
                .claim("isTempPassword", customUserDetails.getIsTempPassword().name()) // 비밀번호 상태 추가
                .issuedAt(Date.from(issuedAt)) // 현재 시간 추가
                .expiration(Date.from(expiration)) //만료 시간 추가
                .signWith(secretKey) //signature 추가
                .compact(); //합치기
    }

    // principalDetails 객체에 대해 새로운 JWT 액세스 토큰을 생성
    public String createJwtAccessToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(accessExpMs);
        return tokenProvider(customUserDetails, expiration);
    }

    // principalDetails 객체에 대해 새로운 JWT 리프레시 토큰을 생성
    public String createJwtRefreshToken(CustomUserDetails customUserDetails) {
        Instant expiration = Instant.now().plusMillis(refreshExpMs);
        String refreshToken = tokenProvider(customUserDetails, expiration);

        // Redis에 Refresh Token 저장
        refreshTokenService.saveRefreshToken(customUserDetails.getUsername(), refreshToken);
        return refreshToken;
    }

    // 제공된 리프레시 토큰을 기반으로 JwtDto 쌍을 다시 발급
    public AuthResDTO.JwtResDTO reissueToken(String refreshToken) throws SignatureException {

        // refreshToken 에서 user 정보를 가져와서 새로운 토큰을 발급 (발급 시간, 유효 시간(reset)만 새로 적용)
        CustomUserDetails userDetails = new CustomUserDetails(
                getEmail(refreshToken),
                null,
                getRoles(refreshToken),
                getIsTempPassword(refreshToken)
        );
        log.info("[ JwtUtil ] 새로운 토큰을 재발급 합니다.");

        // 재발급
        return new AuthResDTO.JwtResDTO(
                createJwtAccessToken(userDetails),
                createJwtRefreshToken(userDetails)
        );
    }

    // HTTP 요청의 'Authorization' 헤더에서 JWT 액세스 토큰을 검색
    public String resolveAccessToken(HttpServletRequest request) {
        log.info("[ JwtUtil ] 헤더에서 토큰을 추출합니다.");
        String tokenFromHeader = request.getHeader("Authorization");

        if (tokenFromHeader == null || !tokenFromHeader.startsWith("Bearer ")) {
            log.warn("[ JwtUtil ] Request Header 에 토큰이 존재하지 않습니다.");
            return null;
        }

        log.info("[ JwtUtil ] 헤더에 토큰이 존재합니다.");

        return tokenFromHeader.split(" ")[1]; //Bearer 와 분리
    }


    public void validateToken(String token) {
        log.info("[ JwtUtil ] 토큰의 유효성을 검증합니다.");
        try {
            // 구문 분석 시스템의 시계가 JWT를 생성한 시스템의 시계 오차 고려
            // 약 3분 허용.
            long seconds = 3 *60;
            boolean isExpired = Jwts
                    .parser()
                    .clockSkewSeconds(seconds)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
            if (isExpired) {
                log.info("만료된 JWT 토큰입니다.");
            }

        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            //원하는 Exception throw
            throw new SecurityException("잘못된 토큰입니다.");
        } catch (ExpiredJwtException e) {
            //원하는 Exception throw
            throw new ExpiredJwtException(null, null, "만료된 JWT 토큰입니다.");
        }
    }
    // 레디스에서 리프레시 토큰의 보관 시간 관련 메서드
    public long getExpiration(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.getTime() - System.currentTimeMillis();
    }
}