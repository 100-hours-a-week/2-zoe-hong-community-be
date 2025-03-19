package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserJoinRequest {
    private String email;
    private String password;
    private String nickname;
    private String profileImgUrl;
}
