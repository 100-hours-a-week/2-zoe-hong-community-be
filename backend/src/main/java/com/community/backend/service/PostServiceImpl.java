package com.community.backend.service;

import com.community.backend.domain.Liked;
import com.community.backend.domain.LikedId;
import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.repository.CommentRepository;
import com.community.backend.repository.LikedRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikedRepository likedRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<PostCardDTO> getPostList() {
        List<PostCardDTO> res = new ArrayList<>();

        postRepository.findAll().forEach(post -> {
            User user = post.getUser();
            UserDTO userDTO = new UserDTO(user.getNickname(), user.getProfileImgUrl());

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
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(post.getUser().getId()).orElseThrow(EntityNotFoundException::new);
        UserDTO userDTO = new UserDTO(user.getNickname(), user.getProfileImgUrl());

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
    public Long save(Long userId, PostRequest req) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post.setUser(userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
        postRepository.save(post);

        return post.getId();
    }

    @Override
    public Long update(Long userId, Long postId, PostRequest req) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
        if (!post.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "게시물을 수정할 권한이 없습니다.");
        }

        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());

        postRepository.save(post);
        return post.getId();
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

    @Override
    public Boolean toggleLike(Long userId, Long postId) {
        Optional<Liked> liked = likedRepository.findByUserIdAndPostId(userId, postId);

        if (liked.isPresent()) {
            likedRepository.delete(liked.get());
            return false;
        } else {
            LikedId likedId = new LikedId(userId, postId);

            Liked newLike = new Liked();
            newLike.setId(likedId);
            newLike.setUser(userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
            newLike.setPost(postRepository.findById(postId).orElseThrow(EntityNotFoundException::new));

            likedRepository.save(newLike);
            return true;
        }
    }
}
