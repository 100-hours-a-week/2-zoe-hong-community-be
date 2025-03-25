package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ProfileRequest {
    private String nickname;
    private MultipartFile profileImg;
}
