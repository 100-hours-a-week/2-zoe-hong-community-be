package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserSessionDTO implements Serializable {
    private static final Long serialVersionUID = 1L;
    private Long userId;
}
