package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String imageUrl;
    private Long createdBy;
}
