package com.people.findclothes.controller;

import com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo.RequestGoogleInfo;
import com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo.RequestKakaoInfo;
import com.people.findclothes.util.OAuth2.requestOAtuh2UserInfo.RequestNaverInfo;
import com.people.findclothes.dto.request.RequestUserLoginDto;
import com.people.findclothes.dto.response.ResponseErrorDto;
import com.people.findclothes.service.UserService;
import com.people.findclothes.util.OAuth2.OAuthRequestFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "로그인, 로그아웃 관련 인증 API")
public class AuthenticationController {

    private final UserService userService;
    private final RequestGoogleInfo requestGoogleInfo;
    private final RequestKakaoInfo requestKakaoInfo;
    private final RequestNaverInfo requestNaverInfo;
    private final OAuthRequestFactory oAuthRequestFactory;

    @Operation(summary = "일반 로그인", description = "입력 정보와 유저 정보가 일치하여 로그인 성공 시 jwt 반환",
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

    @Operation(summary = "소셜 로그인 - 구글", description = "인증 코드를 통해 액세스 토큰과 사용자 정보를 요청하여 성공 시 jwt 반환",
            responses = {@ApiResponse(responseCode = "200", description = "유저 id가 담긴 jwt을 성공적으로 반환")}
    )
    @GetMapping("/login/social/google")
    public ResponseEntity<String> googleLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(userService.socialLogin("google", oAuthRequestFactory.getOAuth2UserInfo(requestGoogleInfo, code)));
    }

    @Operation(summary = "소셜 로그인 - 카카오", description = "인증 코드를 통해 액세스 토큰과 사용자 정보를 요청하여 성공 시 jwt 반환",
            responses = {@ApiResponse(responseCode = "200", description = "유저 id가 담긴 jwt을 성공적으로 반환")}
    )
    @GetMapping("/login/social/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(userService.socialLogin("kakao", oAuthRequestFactory.getOAuth2UserInfo(requestKakaoInfo, code)));
    }

    @Operation(summary = "소셜 로그인 - 네이버", description = "인증 코드를 통해 액세스 토큰과 사용자 정보를 요청하여 성공 시 jwt 반환",
            responses = {@ApiResponse(responseCode = "200", description = "유저 id가 담긴 jwt을 성공적으로 반환")}
    )
    @GetMapping("/login/social/naver")
    public ResponseEntity<String> naverLogin(@RequestParam("code") String code) {
        return ResponseEntity.ok(userService.socialLogin("naver", oAuthRequestFactory.getOAuth2UserInfo(requestNaverInfo, code)));
    }

    @Operation(summary = "로그아웃", description = "DB에서 로그인 유저의 jwt 삭제",
            responses = {@ApiResponse(responseCode = "200", description = "jwt를 성공적으로 삭제")}
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return ResponseEntity.ok().build();
    }
}