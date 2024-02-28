package com.people.findclothes.domain.constant;

public enum UserRole {
    MEMBER("일반 회원"), ADMIN("관리자");

    private final String displayValue;

    UserRole(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
