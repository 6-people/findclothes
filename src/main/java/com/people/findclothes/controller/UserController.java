package com.people.findclothes.controller;

import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.dto.response.ResponseErrorDto;
import com.people.findclothes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "입력 정보와 유저 정보가 일치하여 로그인 성공 시 jwt 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "유저 id가 담긴 jwt을 성공적으로 반환"),
                    @ApiResponse(responseCode = "400", description = "해당 id의 유저가 존재하지 않음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "해당 id의 유저와 비밀번호가 일치하지 않음",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestUserLoginDto requestDto) {
        return ResponseEntity.ok(userService.login(requestDto));
    }

    @Operation(summary = "로그아웃", description = "DB에서 로그인 유저의 jwt 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "jwt를 성공적으로 삭제")
            }
    )
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RequestUserSaveDto requestDto) {
        userService.saveUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}