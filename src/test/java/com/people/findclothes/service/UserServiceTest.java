package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.exception.UserAlreadyExistsException;
import com.people.findclothes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입 - 존재하지 않는 사용자 정보이기 때문에 성공적으로 저장되는 경우")
    void 회원가입_성공() {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doReturn(false).when(userRepository).existsById(anyString());
        doReturn(false).when(userRepository).existsByEmail(anyString());
        doReturn(false).when(userRepository).existsByNickname(anyString());

        // when
        userService.save(requestUserSaveDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 - 이미 존재하는 아이디가 주어졌기 때문에 예외를 던지는 경우")
    void 회원가입_아이디중복() {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doReturn(true).when(userRepository).existsById(anyString());

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(requestUserSaveDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 - 이미 존재하는 이메일이 주어졌기 때문에 예외를 던지는 경우")
    void 회원가입_이메일중복() {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doReturn(false).when(userRepository).existsById(anyString());
        doReturn(true).when(userRepository).existsByEmail(anyString());

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(requestUserSaveDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 - 이미 존재하는 닉네임이 주어졌기 때문에 예외를 던지는 경우")
    void 회원가입_닉네임중복() {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doReturn(false).when(userRepository).existsById(anyString());
        doReturn(false).when(userRepository).existsByEmail(anyString());
        doReturn(true).when(userRepository).existsByNickname(anyString());

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(requestUserSaveDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("아이디 중복 검사 - 중복이 아닌 경우")
    void 아이디중복검사_중복X() {
        // given
        String id = "id";
        doReturn(false).when(userRepository).existsById(anyString());

        // when
        boolean isDuplicated = userService.isDuplicatedId(id);

        // then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    @DisplayName("아이디 중복 검사 - 중복인 경우")
    void 아이디중복검사_중복O() {
        // given
        String id = "id";
        doReturn(true).when(userRepository).existsById(anyString());

        // when
        boolean isDuplicated = userService.isDuplicatedId(id);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 검사 - 중복이 아닌 경우")
    void 이메일중복검사_중복X() {
        // given
        String email = "email@google.com";
        doReturn(false).when(userRepository).existsByEmail(anyString());

        // when
        boolean isDuplicated = userService.isDuplicatedEmail(email);

        // then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    @DisplayName("이메일 중복 검사 - 중복인 경우")
    void 이메일중복검사_중복O() {
        // given
        String email = "email@google.com";
        doReturn(true).when(userRepository).existsByEmail(anyString());

        // when
        boolean isDuplicated = userService.isDuplicatedEmail(email);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복 검사 - 중복이 아닌 경우")
    void 닉네임중복검사_중복X() {
        // given
        String nickname = "nickname";
        doReturn(false).when(userRepository).existsByNickname(anyString());

        // when
        boolean isDuplicated = userService.isDuplicatedNickname(nickname);

        // then
        assertThat(isDuplicated).isFalse();
    }

    @Test
    @DisplayName("닉네임 중복 검사 - 중복인 경우")
    void 닉네임중복검사_중복O() {
        // given
        String nickname = "nickname";
        doReturn(true).when(userRepository).existsByNickname(anyString());

        // when
        boolean isDuplicated = userService.isDuplicatedNickname(nickname);

        // then
        assertThat(isDuplicated).isTrue();
    }

    public RequestUserSaveDto createRequestUserSaveDto() {
        return RequestUserSaveDto.builder()
                .id("userId")
                .email("email@google.com")
                .nickname("nickname")
                .password("1234")
                .build();
    }
}