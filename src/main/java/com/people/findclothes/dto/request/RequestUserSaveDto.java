package com.people.findclothes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Schema(description = "유저 일반 회원가입 요청 DTO")
public class RequestUserSaveDto {

    @Size(min = 3, max = 25)
    @NotBlank(message = "사용자ID는 필수항목입니다.")
    private String id;

    @NotBlank(message = "닉네임은 필수항목입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

}