package com.people.findclothes.controller;

import com.people.findclothes.dto.request.RequestUserSaveDto;
import com.people.findclothes.dto.request.RequestUserUpdateDto;
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

    @Operation(summary = "회원 정보 변경", description = "닉네임 중복 확인 후 회원 정보 변경",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 정보 변경 성공"),
                    @ApiResponse(responseCode = "400", description = "중복되는 닉네임으로 회원 정보 변경 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @PostMapping("/upadate")
    public ResponseEntity<String> Update(@PathVariable String Id, @RequestBody RequestUserUpdateDto requestDto) {
        userService.modifyInfo(Id, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 삭제", description = "패스워드 확인 후 회원 삭제",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 패스워드로 회원 삭제 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @PostMapping("/delete")
    public ResponseEntity<String> Delete(@PathVariable String Id, String Password) {
        userService.deleteUser(Id, Password);
        return ResponseEntity.ok().build();
    }

}