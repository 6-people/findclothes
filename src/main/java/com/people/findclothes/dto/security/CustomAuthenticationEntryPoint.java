package com.people.findclothes.dto.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.findclothes.domain.constant.ErrorCode;
import com.people.findclothes.dto.response.ResponseErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");

        final Map<String, Object> body = new HashMap<>();
        body.put("status", ErrorCode.ANONYMOUS_USER.getStatus());
        body.put("error", ErrorCode.ANONYMOUS_USER.getError());
        body.put("message", "로그인이 필요합니다.");
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }

}
