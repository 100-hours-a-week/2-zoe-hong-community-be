package com.community.backend.service;

import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;

import java.util.List;

public interface PostService {
    List<PostCardDTO> getPostList();
    PostDTO getPostById(Long postId);
    Long save(Long userId, PostRequest req);
    Long update(Long userId, Long postId, PostRequest req);
    void delete(Long userId, Long postId);

    // 좋아요
    Boolean toggleLike(Long userId, Long postId);
}
