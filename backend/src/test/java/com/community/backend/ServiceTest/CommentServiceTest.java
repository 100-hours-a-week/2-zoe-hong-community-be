package com.community.backend.ServiceTest;

import com.community.backend.dto.*;
import com.community.backend.repository.CommentRepository;
import com.community.backend.service.CommentService;
import com.community.backend.service.PostService;
import com.community.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired PostService postService;
    @Autowired UserService userService;
    @Autowired MockFileGenerator mg;

    @Test
    public void 댓글_목록_조회() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        UserJoinRequest joinReq = new UserJoinRequest(
                "aaa@gmail.com",
                "aA1!word",
                "aaa",
                mockFile
        );
        Long userId = userService.join(joinReq);

        UserLoginRequest loginReq = new UserLoginRequest(
                "aaa@gmail.com",
                "aA1!word"
        );

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        // when
        CommentRequest commentReq = new CommentRequest("content");
        Long commentId = commentService.save(userId, postId, commentReq);
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        // then
        assert commentList.size() == 1;
    }

    @Test
    public void 댓글_추가() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        UserJoinRequest joinReq = new UserJoinRequest(
                "aaa@gmail.com",
                "aA1!word",
                "aaa",
                mockFile
        );
        Long userId = userService.join(joinReq);

        UserLoginRequest loginReq = new UserLoginRequest(
                "aaa@gmail.com",
                "aA1!word"
        );

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        // when
        CommentRequest commentReq = new CommentRequest("content");
        Long commentId = commentService.save(userId, postId, commentReq);

        // then
        assert commentRepository.existsById(commentId);

    }

    @Test
    public void 댓글_수정() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        UserJoinRequest joinReq = new UserJoinRequest(
                "aaa@gmail.com",
                "aA1!word",
                "aaa",
                mockFile
        );
        Long userId = userService.join(joinReq);

        UserLoginRequest loginReq = new UserLoginRequest(
                "aaa@gmail.com",
                "aA1!word"
        );

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        CommentRequest commentReq = new CommentRequest("content");
        Long commentId = commentService.save(userId, postId, commentReq);

        // when
        CommentRequest newCommentReq = new CommentRequest("newContent");
        Long resultId = commentService.update(userId, commentId, newCommentReq);

        // then
        assert commentRepository.getReferenceById(resultId).getContent().equals(newCommentReq.getContent());
    }

    @Test
    public void 댓글_삭제() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        UserJoinRequest joinReq = new UserJoinRequest(
                "aaa@gmail.com",
                "aA1!word",
                "aaa",
                mockFile
        );
        Long userId = userService.join(joinReq);

        UserLoginRequest loginReq = new UserLoginRequest(
                "aaa@gmail.com",
                "aA1!word"
        );

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        CommentRequest commentReq = new CommentRequest("content");
        Long commentId = commentService.save(userId, postId, commentReq);

        // when
        commentService.delete(userId, commentId);

        // then
        assert !commentRepository.existsById(commentId);
    }
}
