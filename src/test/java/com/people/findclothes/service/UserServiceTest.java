package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.domain.constant.UserRole;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.exception.UserAlreadyExistsException;
import com.people.findclothes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;
    @Test
    @DisplayName("유저 회원 가입 성공 테스트")
    void saveUserSuccess() {
        RequestUserSaveDto requestUserSaveDto = RequestUserSaveDto.builder()
                .id("userId")
                .nickname("nickname")
                .password("1234")
                .email("email@google.com")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.existsById(anyString())).thenReturn(false);
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        userService.saveUser(requestUserSaveDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("유저 회원 가입 실패 테스트 - 중복된 회원 정보")
    void saveUserFailDueToDuplicatedInfo() {
        // given
        RequestUserSaveDto requestUserSaveDto = RequestUserSaveDto.builder()
                .id("userId")
                .nickname("nickname")
                .password("1234")
                .email("email@google.com")
                .build();
        when(userRepository.existsById(anyString())).thenReturn(true); // Id 중복 가정

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(requestUserSaveDto));
        verify(userRepository, never()).save(any(User.class));
    }
}