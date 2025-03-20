package com.community.backend.service;

import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.repository.CommentRepository;
import com.community.backend.repository.LikedRepository;
import com.community.backend.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikedRepository likedRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<PostCardDTO> getPostList() {
        List<PostCardDTO> res = new ArrayList<>();

        postRepository.findAll().forEach(post -> {
            User user = post.getUser();
            UserDTO userDTO = new UserDTO(user.getId(), user.getNickname(), user.getProfileImgUrl());

            Long likeCount = (long) likedRepository.findByPostId(post.getId()).size();
            Long commentCount = (long) commentRepository.findByPostId(post.getId()).size();

            PostCardDTO dto = new PostCardDTO(
                    post.getId(),
                    post.getTitle(),
                    userDTO,
                    post.getCreatedAt(),
                    likeCount,
                    post.getViewCount(),
                    commentCount
            );
            res.add(dto);
        });
        return res;
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        User user = post.getUser();
        UserDTO userDTO = new UserDTO(user.getId(), user.getNickname(), user.getProfileImgUrl());

        Long likeCount = (long) likedRepository.findByPostId(post.getId()).size();
        Long commentCount = (long) commentRepository.findByPostId(post.getId()).size();

        return new PostDTO(
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                userDTO,
                post.getCreatedAt(),
                likeCount,
                post.getViewCount(),
                commentCount
        );
    }

    @Override
    public void save(PostRequest req) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        postRepository.save(post);
    }

    @Override
    public void delete(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 게시글을 수정/삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}
