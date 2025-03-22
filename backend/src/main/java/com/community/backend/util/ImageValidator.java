package com.community.backend.util;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageValidator {
    public void checkImage(String ImageUrl) {
        if (ImageUrl == null) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }
    }
}
