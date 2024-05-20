package com.people.findclothes.dto.request;

import com.people.findclothes.domain.User;
import com.people.findclothes.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Schema(description = "유저 일반 회원 정보 변경 요청 DTO")
public class RequestUserUpdateDto {

    @Schema(description = "유저 아이디", example = "user")
    public String id;

    @Schema(description = "유저 닉네임", example = "nickname")
    private String nickname;


    @Schema(description = "유저 비밀번호", example = "1111")
    public String password;

    public void updateFields(RequestUserUpdateDto newUserDto) {
        if (newUserDto.getNickname() != null) {
            this.nickname = newUserDto.getNickname();
        }
        if (newUserDto.getPassword() != null) {
            this.password = newUserDto.getPassword();
        }
    }
    public static RequestUserUpdateDto from(User entity) {
        return RequestUserUpdateDto.builder()
                .id(entity.getId())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .build();
    }
}