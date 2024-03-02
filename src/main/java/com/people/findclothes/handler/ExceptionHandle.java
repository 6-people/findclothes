package com.people.findclothes.handler;

import com.people.findclothes.domain.constant.ErrorCode;
import com.people.findclothes.dto.response.ResponseErrorDto;
import com.people.findclothes.exception.PasswordMismatchException;
import com.people.findclothes.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseErrorDto> userNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseErrorDto.toDto(ErrorCode.USER_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ResponseErrorDto> passwordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseErrorDto.toDto(ErrorCode.PASSWORD_MISMATCH, ex.getMessage()));
    }

}