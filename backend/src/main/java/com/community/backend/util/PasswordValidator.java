package com.community.backend.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {
    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public void checkPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("비밀번호가 존재하지 않습니다.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("유효하지 않은 비밀번호 형식입니다.");
        }
    }

    private boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
}
