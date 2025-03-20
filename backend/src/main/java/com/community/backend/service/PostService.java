package com.community.backend.service;

import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;

import java.util.List;

public interface PostService {
    public List<PostCardDTO> getPostList();
    public PostDTO getPostById(Long id);
    public void save(PostRequest req);
    public void delete(Long userId, Long postId);
}
