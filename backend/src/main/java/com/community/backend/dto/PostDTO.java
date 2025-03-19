package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostDTO {
    private String title;
    private String content;
    private String imageUrl;
    private UserDTO user;
    private Timestamp createdAt;
    private Long likesCount;
    private Long viewsCount;
    private Long commentsCount;
}
