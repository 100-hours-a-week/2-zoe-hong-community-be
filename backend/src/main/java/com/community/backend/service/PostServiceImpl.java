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
import com.community.backend.util.ImageHandler;
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
    private final UserRepository userRepository;
    private final ImageHandler imageHandler;

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
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(post.getUser().getId()).orElseThrow(EntityNotFoundException::new);
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
    public Long save(Long userId, PostRequest req) {
        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setImageUrl(imageHandler.saveImage(req.getImage(), false));
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
        post.setImageUrl(imageHandler.saveImage(req.getImage(), false));

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
    public Long toggleLike(Long userId, Long postId) {
        if (isLiked(userId, postId)) {
            unlike(userId, postId);
        } else {
            like(userId, postId);
        }
        return getLikeCount(postId);
    }

    @Override
    public Boolean isLiked(Long userId, Long postId) {
        return likedRepository.findByPostId(postId).stream().anyMatch(like -> like.getUser().getId().equals(userId));
    }

    private void unlike(Long userId, Long postId) {
        likedRepository.findByUserIdAndPostId(userId, postId)
                .ifPresent(likedRepository::delete);
    }

    private void like(Long userId, Long postId) {
        LikedId likedId = new LikedId(userId, postId);
        Liked liked = new Liked();

        liked.setId(likedId);
        liked.setUser(userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
        liked.setPost(postRepository.findById(postId).orElseThrow(EntityNotFoundException::new));

        likedRepository.save(liked);
    }

    private Long getLikeCount(Long postId) {
        return (long) likedRepository.findByPostId(postId).size();
    }
}
