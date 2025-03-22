package com.community.backend.service;

import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;

import java.util.List;

public interface PostService {
    List<PostCardDTO> getPostList();
    PostDTO getPostById(Long postId);
    Long save(PostRequest req);
    Long update(Long postId, PostRequest req);
    void delete(Long postId, Long userId);

    // 좋아요
    Boolean toggleLike(Long userId, Long postId);
}
