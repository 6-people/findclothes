package com.people.findclothes.controller;

import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.request.RequestUserUpdateNicknameDto;
import com.people.findclothes.dto.request.RequestUserUpdatePasswordDto;
import com.people.findclothes.dto.response.ResponseErrorDto;
import com.people.findclothes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/nickname")
    public ResponseEntity<String> getInfo(@RequestParam String id) {
        return ResponseEntity.ok(userService.getNickname(id));
    }

    @Operation(summary = "회원 닉네임 변경", description = "닉네임 중복 확인 후 회원 닉네임 변경",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 닉네임 변경 성공"),
                    @ApiResponse(responseCode = "400", description = "중복된 닉네임으로 회원 정보 변경 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @PutMapping("/updateNickname")
    public ResponseEntity<String> updateNickname(@RequestBody RequestUserUpdateNicknameDto requestDto) {
        userService.updateNickname(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 비밀번호 변경", description = "비밀번호 확인 후 새로운 비밀번호로 변경",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 비밀번호 변경 성공"),
                    @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않아 새로운 비밀번호로 변경 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @PutMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody RequestUserUpdatePasswordDto requestDto) {
        userService.updatePassword(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 삭제", description = "비밀번호 확인 후 회원 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않아 회원 삭제 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestBody RequestUserLoginDto requestDto) {
        userService.delete(requestDto);
        return ResponseEntity.ok().build();
    }

}