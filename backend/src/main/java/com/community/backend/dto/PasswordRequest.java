package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordRequest {
    private Long id;
    private String password;
}
