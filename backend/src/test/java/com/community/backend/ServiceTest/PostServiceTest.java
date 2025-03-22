package com.community.backend.ServiceTest;

import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;
import com.community.backend.repository.PostRepository;
import com.community.backend.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;

    @Test
    public void 게시글_목록_조회() {
        // when
        List<PostCardDTO> postList = postService.getPostList();

        // then
        assert postList.size() == 1;
    }

    @Test
    public void 게시글_조회() {
        // given
        Long postId = 1L;

        // when
        PostDTO dto = postService.getPostById(postId);

        // then
        assert dto.getTitle().equals(postRepository.findById(postId).get().getTitle());
    }

    @Test
    public void 게시글_추가() {
        // given
        PostRequest req = new PostRequest(
                "title",
                "content",
                "image url",
                1L
        );

        // when
        Long postId = postService.save(req);

        // then
        assert postRepository.findById(postId).get().getTitle().equals(req.getTitle());
    }

    @Test
    public void 게시글_수정() {
        // given
        Long postId = 1L;
        PostRequest req = new PostRequest(
                "new title",
                "content",
                "image url",
                1L
        );

        // when
        Long result = postService.update(postId, req);

        // then
        assert postRepository.findById(result).get().getTitle().equals(req.getTitle());
    }

    @Test
    public void 게시글_삭제() {
        // given
        Long postId = 1L;
        Long userId = 1L;

        // when
        postService.delete(postId, userId);

        // then
        assert postRepository.findById(postId).isEmpty();
    }

    @Test
    public void 게시글_좋아요() {
        // given
        Long postId = 1L;
        Long userId = 1L;

        // when
        Boolean result = postService.toggleLike(postId, userId);

        // then
        assert !result;
    }
}
