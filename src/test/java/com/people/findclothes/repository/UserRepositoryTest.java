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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 저장")
    @Test
    public void saveUser() {
        // given
        User user = User.builder()
                .id("userId")
                .password("1234")
                .email("email@google.com")
                .nickname("nickname")
                .name("신짱구")
                .userRole(UserRole.MEMBER)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getUserRole()).isEqualTo(user.getUserRole());
    }

    @DisplayName("유저 조회")
    @Test
    public void findUser() {
        // given
        User user = User.builder()
                .id("userId")
                .password("1234")
                .email("email@google.com")
                .nickname("nickname")
                .name("신짱구")
                .userRole(UserRole.MEMBER)
                .build();

        // when
        User savedUser = userRepository.save(user);
        User findUser = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(savedUser).isSameAs(findUser);
        assertThat(findUser.getCreateAt()).isEqualTo(savedUser.getCreateAt());
    }
}
