package com.community.backend.controller;

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
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            List<PostCardDTO> list = postService.getPostList();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "게시글 목록을 불러오는 과정에 오류가 발생했습니다.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(HttpSession session, @ModelAttribute PostRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long postId = postService.save(user.getUserId(), req);
            return new ResponseEntity<>(postId + "번 게시물을 생성했습니다.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() + "유효성 검사에 실패했습니다.");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "게시글을 생성하는 과정에 오류가 발생했습니다.");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findPostById(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            PostDTO postDTO = postService.getPostById(postId);
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "게시글을 불러오는 과정에 오류가 발생했습니다.");
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(HttpSession session, @PathVariable Long postId, @ModelAttribute PostRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        PostDTO post = postService.getPostById(postId);
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "게시글을 수정할 권한이 없습니다.");
        }

         try {
             Long resultPostId = postService.update(user.getUserId(), postId, req);
             return new ResponseEntity<>(resultPostId, HttpStatus.OK);
         } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() + "유효성 검사에 실패했습니다.");
         } catch (RuntimeException e) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "게시글을 수정하는 과정에 오류가 발생했습니다.");
         }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        PostDTO post = postService.getPostById(postId);
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "게시글을 삭제할 권한이 없습니다.");
        }

        try {
            postService.delete(user.getUserId(), postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "게시글을 삭제하는 과정에 오류가 발생했습니다.");
        }
    }

    /**
     * 좋아요
     */

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "좋아요 할 권한이 없습니다.");
        }

        try {
            boolean isAlreadyLiked = postService.isLiked(user.getUserId(), postId);
            Long likes = postService.toggleLike(user.getUserId(), postId);
            String message = isAlreadyLiked ? "좋아요 취소되었습니다." : "좋아요 등록되었습니다.";
            return ResponseEntity.ok().body(Map.of("likes", likes, "message", message));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "좋아요 처리 중 오류가 발생하였습니다.");
        }
    }
}
