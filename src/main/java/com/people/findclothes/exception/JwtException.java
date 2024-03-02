package com.people.findclothes.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}