package com.people.findclothes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Schema(description = "유저 일반 회원 정보 변경 요청 DTO")
public class RequestUserUpdateNicknameDto {

    @Schema(description = "유저 아이디", example = "user")
    private String id;

    @Schema(description = "유저 닉네임", example = "nickname")
    @NotBlank(message = "닉네임은 필수항목입니다.")
    private String newNickname;

}