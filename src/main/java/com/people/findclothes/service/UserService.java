package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.security.CustomUserDetails;
import com.people.findclothes.exception.PasswordMismatchException;
import com.people.findclothes.exception.UserNotFoundException;
import com.people.findclothes.repository.UserRepository;
import com.people.findclothes.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = true)
    public String login(RequestUserLoginDto requestDto) {
        // 로그인 검사 : 유저 정보 존재 여부, 비밀번호 일치 여부
        User findUser = userRepository.findById(requestDto.getId()).orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 존재하지 않아 로그인 실패하였습니다."));
//        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword()))
//            throw new LoginFailureException("비밀번호가 일치하지 않아 로그인 실패하였습니다.");
        if (!requestDto.getPassword().equals(findUser.getPassword()))
            throw new PasswordMismatchException("비밀번호가 일치하지 않아 로그인 실패하였습니다.");

        // 토큰 생성
        String token = jwtTokenProvider.createToken(findUser.getId());

        // 레디스에 토큰 저장
        redisTemplate.opsForValue().set("JWT_TOKEN:" + requestDto.getId(), token);

        return token;
    }

    @Transactional(readOnly = true)
    public void logout() {
        // spring security 상 인증된 user detail의 usernamer과 일치하는 jwt를 삭제
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        redisTemplate.delete("JWT_TOKEN:" + customUserDetails.getUsername());
    }
}

// TODO : 회원가입 시 비밀번호 PasswordEncoder 적용 시 UserService 수정