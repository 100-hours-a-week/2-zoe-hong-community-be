package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileRequest {
    private Long id;
    private String nickname;
    private String profileImgUrl;
}
