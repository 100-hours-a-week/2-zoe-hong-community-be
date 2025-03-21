package com.community.backend.service;

import com.community.backend.domain.Comment;
import com.community.backend.domain.User;
import com.community.backend.dto.CommentDTO;
import com.community.backend.dto.CommentRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.repository.CommentRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentDTO> getCommentList(Long postId) {
        List<CommentDTO> res = new ArrayList<>();

        commentRepository.findAll().forEach(comment -> {
            User user = comment.getUser();
            UserDTO userDTO = new UserDTO(user.getId(), user.getNickname(), user.getProfileImgUrl());

            CommentDTO dto = new CommentDTO(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    userDTO
            );
            res.add(dto);
        });
        return res;
    }

    @Override
    public Long save(CommentRequest req) {
        Comment comment = new Comment();
        comment.setPost(postRepository.findById(req.getPostId()).orElse(null));
        comment.setUser(userRepository.findById(req.getUserId()).orElse(null));
        comment.setContent(req.getContent());
        commentRepository.save(comment);

        return comment.getId();
    }

    @Override
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 댓글을 수정/삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }
}
