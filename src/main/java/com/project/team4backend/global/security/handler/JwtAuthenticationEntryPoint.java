package com.project.team4backend.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.domain.auth.exception.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);
        CustomResponse<Object> errorResponse = CustomResponse.onFailure(
                AuthErrorCode._UNAUTHORIZED.getCode(),
                AuthErrorCode._UNAUTHORIZED.getMessage(),
                null
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}