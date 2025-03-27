package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;
import com.community.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> findAllPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            List<PostCardDTO> posts = postService.getPostList();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "posts", posts
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@ModelAttribute PostRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = (Long) authentication.getPrincipal();
            Long postId = postService.save(userId, req);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "id", postId
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findPostById(@PathVariable Long postId, @RequestParam(required = false) Boolean isEdit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            if (!isEdit) {
                postService.increaseViewCount(postId);
            }
            PostDTO post = postService.getPostById(postId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "post", post
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{postId}/edit")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @ModelAttribute PostRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        PostDTO post = postService.getPostById(postId);
        Long userId = (Long) authentication.getPrincipal();
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "게시글을 수정할 권한이 없습니다.");
        }

         try {
             Long resultPostId = postService.update(userId, postId, req);
             return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                     "success", true,
                     "id", resultPostId
             ));
         } catch (IllegalArgumentException e) {
             throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
         } catch (RuntimeException e) {
             throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
         }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        PostDTO post = postService.getPostById(postId);
        Long userId = (Long) authentication.getPrincipal();

        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "게시글을 삭제할 권한이 없습니다.");
        }

        try {
            postService.delete(userId, postId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "id", postId
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 좋아요
     */
    @GetMapping("/{postId}/like")
    public ResponseEntity<?> isLiked(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }


        try {
            Long userId = (Long) authentication.getPrincipal();
            Boolean liked = postService.isLiked(userId, postId);
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "liked", liked
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = (Long) authentication.getPrincipal();
            boolean isAlreadyLiked = postService.isLiked(userId, postId);
            Long likes = postService.toggleLike(userId, postId);
            String message = isAlreadyLiked ? "좋아요 취소되었습니다." : "좋아요 등록되었습니다.";
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "likes", likes,
                    "message", message
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
