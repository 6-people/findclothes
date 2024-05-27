package com.people.findclothes;

import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.dto.request.RequestUserUpdateNicknameDto;
import com.people.findclothes.dto.request.RequestUserUpdatePasswordDto;

public class CreateObject {
    public static RequestUserSaveDto createRequestUserSaveDto() {
        return RequestUserSaveDto.builder()
                .id("userId")
                .email("email@google.com")
                .nickname("nickname")
                .password("1234")
                .build();
    }

    public static RequestUserUpdateNicknameDto createRequestUserUpdateNicknameDto() {
        return RequestUserUpdateNicknameDto.builder()
                .id("userId")
                .newNickname("newNickname")
                .build();
    }

    public static RequestUserUpdatePasswordDto createRequestUserUpdatePasswordDto(String password) {
        return RequestUserUpdatePasswordDto.builder()
                .id("userId")
                .oldPassword(password)
                .newPassword("newPassword")
                .build();
    }

    public static RequestUserLoginDto createRequestUserLoginDto() {
        return RequestUserLoginDto.builder()
                .id("userId")
                .password("password")
                .build();
    }
}
