package com.people.findclothes.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}