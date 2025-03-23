package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private String content;
    private Timestamp createdAt;
    private UserDTO user;
}