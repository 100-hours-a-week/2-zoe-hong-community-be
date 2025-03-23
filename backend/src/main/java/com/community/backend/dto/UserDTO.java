package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String nickname;
    private String profileImgUrl;
}
