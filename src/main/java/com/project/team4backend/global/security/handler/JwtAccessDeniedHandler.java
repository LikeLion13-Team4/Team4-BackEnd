package com.project.team4backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.domain.auth.exception.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);
        CustomResponse<Object> errorResponse = CustomResponse.onFailure(
                AuthErrorCode._FORBIDDEN.getCode(),
                AuthErrorCode._FORBIDDEN.getMessage(),
                null
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
