package com.people.findclothes.dto.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.findclothes.domain.constant.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");

        final Map<String, Object> body = new HashMap<>();
        body.put("status", ErrorCode.ACCESS_DENIED.getStatus());
        body.put("error", ErrorCode.ACCESS_DENIED.getError());
        body.put("message", "접근 권한이 없습니다.");
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(body));
    }

}