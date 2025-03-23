package com.community.backend.controller;

import com.community.backend.dto.CommentDTO;
import com.community.backend.dto.CommentRequest;
import com.community.backend.dto.UserSessionDTO;
import com.community.backend.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getComments(HttpSession session, @PathVariable Long postId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            List<CommentDTO> comments = commentService.getCommentList(postId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "댓글 목록을 불러오는 과정에 오류가 발생했습니다.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createComment(HttpSession session, Long postId, @RequestBody CommentRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long commentId = commentService.save(user.getUserId(), postId, req);
            return new ResponseEntity<>(commentId + ": 댓글이 생성되었습니다.", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "댓글을 생성하는 과정에 오류가 발생했습니다.");
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(HttpSession session, @PathVariable Long commentId, @RequestBody CommentRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long resultCommentId = commentService.update(user.getUserId(), commentId, req);
            return new ResponseEntity<>(resultCommentId + ":번 댓글이 수정되었습니다.", HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "댓글을 수정하는 과정에 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(HttpSession session, @PathVariable Long commentId) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            commentService.delete(user.getUserId(), commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "댓글을 수정하는 과정에 오류가 발생했습니다.");
        }
    }
}
