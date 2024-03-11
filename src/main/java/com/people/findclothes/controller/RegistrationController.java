package com.people.findclothes.controller;

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
@RequestMapping("/register")
@RequiredArgsConstructor
@Tag(name = "Registration", description = "일반 회원가입 관련 API")
public class RegistrationController {

    private final UserService userService;

    @Operation(summary = "일반 회원가입", description = "입력 정보와 중복되는 유저 정보가 없다면 회원가입",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                    @ApiResponse(responseCode = "400", description = "중복되는 회원 정보로 회원가입 실패",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDto.class)))
            }
    )
    @PostMapping
    public ResponseEntity<String> register(@RequestBody RequestUserSaveDto requestDto) {
        userService.saveUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "id 중복 검사", description = "회원가입 시 입력받은 id와 중복되는 id가 있는지 검사",
            responses = {
                    @ApiResponse(responseCode = "200", description = "id 중복 조회 성공")
            }
    )
    @GetMapping("/isDuplicatedId")
    public ResponseEntity<Boolean> isDuplicatedId(@RequestParam("id") String id) {
        System.out.println("id = " + id);
        return ResponseEntity.ok(userService.isDuplicatedId(id));
    }

    @Operation(summary = "email 중복 검사", description = "회원가입 시 입력받은 email와 중복되는 email이 있는지 검사",
            responses = {
                    @ApiResponse(responseCode = "200", description = "email 중복 조회 성공")
            }
    )
    @GetMapping("/isDuplicatedEmail")
    public ResponseEntity<Boolean> isDuplicatedEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.isDuplicatedEmail(email));
    }

    @Operation(summary = "nickname 중복 검사", description = "회원가입 시 입력받은 nickname과 중복되는 nickname이 있는지 검사",
            responses = {
                    @ApiResponse(responseCode = "200", description = "nickname 중복 조회 성공")
            }
    )
    @GetMapping("/isDuplicatedNickname")
    public ResponseEntity<Boolean> isDuplicatedNickname(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(userService.isDuplicatedNickname(nickname));
    }
}