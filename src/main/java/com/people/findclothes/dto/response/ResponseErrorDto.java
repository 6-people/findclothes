package com.people.findclothes.dto.response;

import com.people.findclothes.domain.constant.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Schema(description = "에러 처리 DTO")
public class ResponseErrorDto {

    @Schema(description = "에러 처리 시간")
    public LocalDateTime timestamp;

    @Schema(description = "에러 상태 코드", example = "403")
    public long status;

    @Schema(description = "에러 원인", example = "Access Denied")
    public String error;

    @Schema(description = "에러 메세지", example = "접근 권한이 없습니다.")
    public String message;

    public static ResponseErrorDto toDto(ErrorCode errorCode, String message) {
        return ResponseErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus())
                .error(errorCode.getError())
                .message(message)
                .build();
    }
}