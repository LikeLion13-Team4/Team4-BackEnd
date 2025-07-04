package com.project.team4backend.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.team4backend.domain.auth.entity.enums.IsTempPassword;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.global.security.CustomUserDetails;
import com.project.team4backend.domain.auth.exception.auth.AuthErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ForcePasswordChangeFilter extends OncePerRequestFilter {
    private static final List<String> ALLOWED_PATHS = List.of(
            "/api/v1/auths/password-reset", "/api/v1/auths/reissue"
    );

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getIsTempPassword() == IsTempPassword.IS_TEMP_PASSWORD) {
                // 허용된 경로 외에는 전부 차단
                String requestURI = request.getRequestURI();
                boolean isAllowed = ALLOWED_PATHS.stream().anyMatch(requestURI::startsWith);
                if (!isAllowed) {
                    log.warn("[ ForcePasswordChangeFilter ] 임시 비밀번호 상태로 허용되지 않은 경로 접근 시도");
                    String errorCode = AuthErrorCode.AUTH_FORBIDDEN.getCode();
                    String errorMessage = AuthErrorCode.AUTH_FORBIDDEN.getMessage();

                    CustomResponse<String> responseBody = CustomResponse.onFailure(errorCode, errorMessage);

                    //JSON 변환
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setCharacterEncoding("UTF-8");

                    //Body 에 토큰이 담긴 Response 쓰기
                    response.getWriter().write(objectMapper.writeValueAsString(responseBody));
                    return;
                }
            }
        }


        filterChain.doFilter(request, response);
    }
}
