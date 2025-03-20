package com.community.backend.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NicknameValidator {
    private static final String NICKNAME_REGEX = "^[^\\s]{1,10}$";
    private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);

    public void checkNickname(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("프로필 이미지가 존재하지 않습니다.");
        }
        if (!isValidNickname(nickname)) {
            throw new IllegalArgumentException("유효하지 않은 닉네임 형식입니다.");
        }
    }

    private boolean isValidNickname(String nickname) {
        return nickname != null && NICKNAME_PATTERN.matcher(nickname).matches();
    }
}
