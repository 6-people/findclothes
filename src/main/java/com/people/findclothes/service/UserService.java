package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.domain.constant.UserRole;
import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.dto.security.CustomUserDetails;
import com.people.findclothes.exception.PasswordMismatchException;
import com.people.findclothes.exception.UserAlreadyExistsException;
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
        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword()))
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

    @Transactional
    public void saveUser(RequestUserSaveDto requestDto) {
        if (isDuplicatedEmail(requestDto.getEmail()) || isDuplicatedId(requestDto.getId()) || isDuplicatedNickname(requestDto.getNickname())) {
            throw new UserAlreadyExistsException();
        }

        userRepository.save(
                User.builder()
                        .id(requestDto.getId())
                        .email(requestDto.getEmail())
                        .nickname(requestDto.getNickname())
                        .userRole(UserRole.MEMBER)
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .build());
    }

    // TODO : task3 -> exception handler

    /**
     * Unique한 값을 가져야하나, email이 중복된 값을 가질 경우를 검증
     *
     * @param email
     */
    public boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Unique한 값을 가져야하나, nickname이 중복된 값을 가질 경우를 검증
     *
     * @param nickname
     */
    public boolean isDuplicatedNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    /**
     * Unique한 값을 가져야하나, id가 중복된 값을 가질 경우를 검증
     *
     * @param id
     */
    public boolean isDuplicatedId(String id) {
        return userRepository.findById(id).isPresent();
    }
}