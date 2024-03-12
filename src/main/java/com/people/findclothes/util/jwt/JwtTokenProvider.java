package com.people.findclothes.util.jwt;

import com.people.findclothes.dto.auth.CustomUserDetails;
import com.people.findclothes.exception.JwtException;
import com.people.findclothes.service.CustumUserDetailService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.key}")
    private String SECRET_KEY;

    private final long tokenValidTime = 30 * 60 * 1000L; // 토큰 유효시간 = 30분
    private final CustumUserDetailService custumUserDetailService;

    // 객체 초기화, SecretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    // 토큰 생성
    public String createToken(String userId) {
        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(userId)) // 정보 저장
                .setIssuedAt(new Date()) // 토큰 발행시간
                .setExpiration(new Date(new Date().getTime() + tokenValidTime)) // 토큰 유효시간
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 암호화 알고리즘, secret 값
                .compact();
    }

    // 인증 정보 조회
    public Authentication getAuthentication(String token) {
        CustomUserDetails customUserDetails = (CustomUserDetails) custumUserDetailService.loadUserByUsername(getUserId(token));
        return new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
    }

    // 토큰에서 User Id 추출
    public String getUserId(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    // 토큰 유효성, 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            throw new JwtException("잘못된 JWT 시그니처입니다.");
        } catch (MalformedJwtException ex) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException ex) {
            throw new JwtException("만료된 토큰입니다.");
        } catch (UnsupportedJwtException ex) {
            throw new JwtException("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            throw new JwtException("토큰에 저장된 정보가 없습니다.");
        }
    }

    // Request의 Header에서 token 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }
}
