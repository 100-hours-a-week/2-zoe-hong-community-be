package com.community.backend.service;

import com.community.backend.dto.CommentDTO;
import com.community.backend.dto.CommentRequest;

import java.util.List;

public interface CommentService {
    public List<CommentDTO> getCommentList(Long postId);
    public void save(CommentRequest req);
    public void delete(Long userId, Long commentId);
}
