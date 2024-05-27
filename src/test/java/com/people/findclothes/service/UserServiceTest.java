package com.people.findclothes.service;

import com.people.findclothes.domain.User;
import com.people.findclothes.domain.constant.UserRole;
import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.dto.request.RequestUserUpdateNicknameDto;
import com.people.findclothes.dto.request.RequestUserUpdatePasswordDto;
import com.people.findclothes.exception.PasswordMismatchException;
import com.people.findclothes.exception.UserAlreadyExistsException;
import com.people.findclothes.exception.UserNotFoundException;
import com.people.findclothes.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static com.people.findclothes.CreateObject.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 가입 - 기존 사용자와 중복되는 정보가 없기 때문에 성공적으로 저장되는 경우")
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
    @DisplayName("회원 가입 - 기존 사용자와 중복된 아이디이기 때문에 예외를 던지는 경우")
    void 회원가입_아이디중복() {
        // given
        RequestUserSaveDto requestUserSaveDto = createRequestUserSaveDto();

        doReturn(true).when(userRepository).existsById(anyString());

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.save(requestUserSaveDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 가입 - 기존 사용자와 중복된 이메일이기 때문에 예외를 던지는 경우")
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
    @DisplayName("회원 가입 - 기존 사용자와 중복된 닉네임이기 때문에 예외를 던지는 경우")
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
    @DisplayName("회원 닉네임 변경 - 중복된 닉네임이 아니며 해당 아이디를 가진 사용자가 존재하기 때문에 성공적으로 저장되는 경우")
    void 회원닉네임변경_성공() {
        // given
        RequestUserUpdateNicknameDto requestDto = createRequestUserUpdateNicknameDto();
        User user = createUser();

        doReturn(false).when(userRepository).existsByNickname(anyString());
        doReturn(Optional.of(user)).when(userRepository).findById(anyString());

        // when
        userService.updateNickname(requestDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 닉네임 변경 - 기존 사용자와 중복된 닉네임이기 때문에 예외를 던지는 경우")
    void 회원닉네임변경_닉네임중복() {
        // given
        RequestUserUpdateNicknameDto requestDto = createRequestUserUpdateNicknameDto();

        doReturn(true).when(userRepository).existsByNickname(anyString());

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> userService.updateNickname(requestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 닉네임 변경 - 해당 아이디를 가진 사용자가 존재하지 않기 때문에 예외를 던지는 경우")
    void 회원닉네임변경_회원정보없음() {
        // given
        RequestUserUpdateNicknameDto requestDto = createRequestUserUpdateNicknameDto();

        doReturn(Optional.empty()).when(userRepository).findById(anyString());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.updateNickname(requestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 비밀번호 변경 - 해당 아이디를 가진 사용자가 존재하며 비밀번호도 일치하기 때문에 성공적으로 저장되는 경우")
    void 회원비밀번호변경_성공() {
        // given
        RequestUserUpdatePasswordDto requestDto = createRequestUserUpdatePasswordDto("1234");
        User user = createUser();

        doReturn(Optional.of(user)).when(userRepository).findById(anyString());
        doReturn(true).when(passwordEncoder).matches(requestDto.getOldPassword(), user.getPassword());

        // when
        userService.updatePassword(requestDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 비밀번호 변경 - 해당 아이디를 가진 사용자가 존재하지 않기 때문에 예외를 던지는 경우")
    void 회원비밀번호변경_회원정보없음() {
        // given
        RequestUserUpdatePasswordDto requestDto = createRequestUserUpdatePasswordDto("1234");

        doReturn(Optional.empty()).when(userRepository).findById(anyString());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(requestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 비밀번호 변경 - 비밀번호가 일치하지 않기 때문에 예외를 던지는 경우")
    void 회원비밀번호변경_비밀번호불일치() {
        // given
        RequestUserUpdatePasswordDto requestDto = createRequestUserUpdatePasswordDto("12345");
        User user = createUser();

        doReturn(Optional.of(user)).when(userRepository).findById(anyString());

        // when & then
        assertThrows(PasswordMismatchException.class, () -> userService.updatePassword(requestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 삭제 - 해당 아이디를 가진 사용자가 존재하며 비밀번호도 일치하기 때문에 성공적으로 삭제되는 경우")
    void 회원삭제_성공() {
        // given
        RequestUserLoginDto requestDto = createRequestUserLoginDto();
        User user = createUser();

        doReturn(Optional.of(user)).when(userRepository).findById(anyString());
        doReturn(true).when(passwordEncoder).matches(requestDto.getPassword(), user.getPassword());

        // when
        userService.delete(requestDto);

        // then
        verify(userRepository, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("회원 삭제 - 해당 아이디를 가진 사용자가 존재하지 않기 때문에 예외를 던지는 경우")
    void 회원삭제_회원정보없음() {
        // given
        RequestUserLoginDto requestDto = createRequestUserLoginDto();

        doReturn(Optional.empty()).when(userRepository).findById(anyString());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.delete(requestDto));
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("회원 삭제 - 비밀번호가 일치하지 않기 때문에 예외를 던지는 경우")
    void 회원삭제_비밀번호불일치() {
        // given
        RequestUserLoginDto requestDto = createRequestUserLoginDto();
        User user = createUser();

        doReturn(Optional.of(user)).when(userRepository).findById(anyString());

        // when & then
        assertThrows(PasswordMismatchException.class, () -> userService.delete(requestDto));
        verify(userRepository, never()).deleteById(anyString());
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

    public User createUser() {
        return User.builder()
                .id("id")
                .email("email")
                .nickname("nickname")
                .userRole(UserRole.MEMBER)
                .password(passwordEncoder.encode("1234"))
                .build();
    }
}