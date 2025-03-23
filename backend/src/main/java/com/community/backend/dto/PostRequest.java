package com.community.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private MultipartFile image;
}
