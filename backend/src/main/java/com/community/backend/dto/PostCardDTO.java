package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostCardDTO {
    private Long id;
    private String title;
    private UserDTO user;
    private Timestamp createdAt;
    private Long likeCount;
    private Long viewCount;
    private Long commentCount;
}
