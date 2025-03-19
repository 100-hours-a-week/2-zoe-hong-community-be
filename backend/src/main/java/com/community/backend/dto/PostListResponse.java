package com.community.backend.dto;

import com.community.backend.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private List<Post> posts;
}
