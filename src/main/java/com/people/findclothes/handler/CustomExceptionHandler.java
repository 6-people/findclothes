package com.people.findclothes.handler;

import com.people.findclothes.domain.constant.ErrorCode;
import com.people.findclothes.dto.response.ResponseErrorDto;
import com.people.findclothes.exception.PasswordMismatchException;
import com.people.findclothes.exception.UserAlreadyExistsException;
import com.people.findclothes.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseErrorDto> userNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(ErrorCode.USER_NOT_FOUND.getStatus())
                .body(ResponseErrorDto.toDto(ErrorCode.USER_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ResponseErrorDto> passwordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity
                .status(ErrorCode.PASSWORD_MISMATCH.getStatus())
                .body(ResponseErrorDto.toDto(ErrorCode.PASSWORD_MISMATCH, ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseErrorDto> userAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(ErrorCode.USER_ALREADY_EXISTS.getStatus())
                .body(ResponseErrorDto.toDto(ErrorCode.USER_ALREADY_EXISTS, ex.getMessage()));
    }

}