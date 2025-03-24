package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;
import com.community.backend.dto.UserSessionDTO;
import com.community.backend.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> findAllPosts(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
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
    public ResponseEntity<?> createPost(HttpSession session, @ModelAttribute PostRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long postId = postService.save(user.getUserId(), req);
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
    public ResponseEntity<?> findPostById(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            PostDTO post = postService.getPostById(postId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "post", post
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(HttpSession session, @PathVariable Long postId, @ModelAttribute PostRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        PostDTO post = postService.getPostById(postId);
        if (!post.getUser().getId().equals(user.getUserId())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "게시글을 수정할 권한이 없습니다.");
        }

         try {
             Long resultPostId = postService.update(user.getUserId(), postId, req);
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
    public ResponseEntity<?> deletePost(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        PostDTO post = postService.getPostById(postId);
        if (!post.getUser().getId().equals(user.getUserId())) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "게시글을 삭제할 권한이 없습니다.");
        }

        try {
            postService.delete(user.getUserId(), postId);
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

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            boolean isAlreadyLiked = postService.isLiked(user.getUserId(), postId);
            Long likes = postService.toggleLike(user.getUserId(), postId);
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
