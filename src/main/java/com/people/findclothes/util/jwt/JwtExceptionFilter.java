package com.people.findclothes.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.people.findclothes.domain.constant.ErrorCode;
import com.people.findclothes.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            setErrorResponse(request, response, ex);
        }
    }

    public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", ErrorCode.JWT_FAILURE.getStatus());
        body.put("error", ErrorCode.JWT_FAILURE.getError());
        body.put("message", ex.getMessage());
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}