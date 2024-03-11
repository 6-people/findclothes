package com.people.findclothes.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    JWT_FAILURE(401, "Unauthorized - Jwt Failure"),
    ANONYMOUS_USER(401, "Unauthorized - Anonymous User"),
    ACCESS_DENIED(403, "Access Denied"),
    USER_NOT_FOUND(404, "User Not Found"),
    PASSWORD_MISMATCH(400, "Password Mismatch"),
    USER_ALREADY_EXISTS(400, "User Already Exists");
    private final int status;
    private final String error;
}