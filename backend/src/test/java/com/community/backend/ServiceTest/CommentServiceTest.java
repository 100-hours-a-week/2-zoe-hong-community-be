package com.community.backend.ServiceTest;

import com.community.backend.dto.CommentDTO;
import com.community.backend.dto.CommentRequest;
import com.community.backend.repository.CommentRepository;
import com.community.backend.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;

    @Test
    public void 댓글_목록_조회() {
        // given
        Long postId = 1L;

        // when
        List<CommentDTO> commentList = commentService.getCommentList(postId);

        // then
        assert commentList.size() == 1;
    }

    @Test
    public void 댓글_추가() {
        // given
        CommentRequest req = new CommentRequest(
                1L,
                1L,
                "content"
        );

        // when
        Long commentId = commentService.save(req);

        // then
        assert commentRepository.existsById(commentId);

    }

    @Test
    public void 댓글_수정() {
        // given
        Long commentId = 1L;
        CommentRequest req = new CommentRequest(
                1L,
                1L,
                "new content"
        );

        // when
        Long result = commentService.update(commentId, req);

        // then
        assert commentRepository.getReferenceById(result).getContent().equals(req.getContent());
    }

    @Test
    public void 댓글_삭제() {
        // given
        Long userId = 1L;
        Long commentId = 13L;

        // when
        commentService.delete(userId, commentId);

        // then
        assert !commentRepository.existsById(commentId);
    }
}
