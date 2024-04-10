package com.people.findclothes.repository;

import com.people.findclothes.domain.User;
import com.people.findclothes.domain.constant.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // ActiveProfiles에 설정한 환경값에 따라 적용
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 저장")
    @Test
    public void save() {
        // given
        User user = createUser();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getUserRole()).isEqualTo(user.getUserRole());
    }

    @DisplayName("아이디로 유저 조회")
    @Test
    public void findById() {
        // given
        User user = createUser();

        // when
        User savedUser = userRepository.save(user);
        User findUser = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(savedUser).isSameAs(findUser);
        assertThat(findUser.getCreateAt()).isEqualTo(savedUser.getCreateAt());
    }

    @DisplayName("해당 이메일로 저장된 유저 존재 여부 조회")
    @Test
    public void existsByEmail() {
        // given
        String unsavedEmail = "unsavedEmail@google.com";
        User user = createUser();

        // when
        User savedUser = userRepository.save(user);
        boolean isExists_unsaved = userRepository.existsByEmail(unsavedEmail);
        boolean isExists_saved = userRepository.existsByEmail(savedUser.getEmail());

        // then
        assertThat(isExists_unsaved).isFalse();
        assertThat(isExists_saved).isTrue();
    }

    @DisplayName("해당 닉네임으로 저장된 유저 존재 여부 조회")
    @Test
    public void existsByNickname() {
        // given
        String unsavedNickname = "unsavedNickname";
        User user = createUser();

        // when
        User savedUser = userRepository.save(user);
        boolean isExists_unsaved = userRepository.existsByNickname(unsavedNickname);
        boolean isExists_saved = userRepository.existsByNickname(savedUser.getNickname());

        // then
        assertThat(isExists_unsaved).isFalse();
        assertThat(isExists_saved).isTrue();
    }

    public User createUser() {
        return User.builder()
                .id("userId")
                .password("1234")
                .email("email@google.com")
                .nickname("nickname")
                .userRole(UserRole.MEMBER)
                .build();
    }
}
