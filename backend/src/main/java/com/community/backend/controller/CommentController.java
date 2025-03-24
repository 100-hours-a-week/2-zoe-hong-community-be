package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.CommentDTO;
import com.community.backend.dto.CommentRequest;
import com.community.backend.dto.UserSessionDTO;
import com.community.backend.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getComments(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            List<CommentDTO> comments = commentService.getCommentList(postId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "comments", comments
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createComment(HttpSession session, @PathVariable Long postId, @RequestBody CommentRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long commentId = commentService.save(user.getUserId(), postId, req);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "id", commentId
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(HttpSession session, @PathVariable Long commentId, @RequestBody CommentRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long resultCommentId = commentService.update(user.getUserId(), commentId, req);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "id", resultCommentId
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(HttpSession session, @PathVariable Long commentId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            commentService.delete(user.getUserId(), commentId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "id", commentId
            ));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
