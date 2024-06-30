package com.people.findclothes.dto;

import com.people.findclothes.domain.User;
import com.people.findclothes.domain.constant.UserRole;
import com.people.findclothes.dto.request.RequestUserUpdateNicknameDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDto {
    private String id;

    private String password;

    private String email;

    private String nickname;

    private UserRole userRole;

    private String provider;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    public static UserDto from(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .userRole(entity.getUserRole())
                .provider(entity.getProvider())
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .password(password)
                .email(email)
                .nickname(nickname)
                .userRole(userRole)
                .provider(provider)
                .createAt(createAt)
                .updateAt(updateAt)
                .build();
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}