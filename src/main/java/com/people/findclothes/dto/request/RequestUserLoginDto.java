package com.people.findclothes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Schema(description = "유저 로그인 요청 DTO")
public class RequestUserLoginDto {

    @Schema(description = "유저 아이디", example = "user")
    public String id;

    @Schema(description = "유저 비밀번호", example = "1111")
    public String password;
}
