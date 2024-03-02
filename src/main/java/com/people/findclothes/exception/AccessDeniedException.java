package com.people.findclothes.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}