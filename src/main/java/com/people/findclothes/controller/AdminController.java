package com.people.findclothes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminController {
    @Operation(summary = "관리자 권한 테스트", description = "유저 권한에 따라 페이지 접근 제한",
            responses = {
                    @ApiResponse(responseCode = "200", description = "접근 성공"),
                    @ApiResponse(responseCode = "403", description = "권한이 없어서 접근 실패")
            }
    )
    @GetMapping
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }
}