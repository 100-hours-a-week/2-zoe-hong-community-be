package com.community.backend.ServiceTest;

import com.community.backend.dto.PostCardDTO;
import com.community.backend.dto.PostDTO;
import com.community.backend.dto.PostRequest;
import com.community.backend.repository.LikedRepository;
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
    @Autowired
    private LikedRepository likedRepository;

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
        Long userId = 1L;
        PostRequest req = new PostRequest(
                "title",
                "content",
                null
        );

        // when
        Long postId = postService.save(userId, req);

        // then
        assert postRepository.findById(postId).get().getTitle().equals(req.getTitle());
    }

    @Test
    public void 게시글_수정() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        PostRequest req = new PostRequest(
                "new title",
                "content",
                null
        );

        // when
        Long result = postService.update(userId, postId, req);

        // then
        assert postRepository.findById(result).get().getTitle().equals(req.getTitle());
    }

    @Test
    public void 게시글_삭제() {
        // given
        Long postId = 1L;
        Long userId = 1L;

        // when
        postService.delete(userId, postId);

        // then
        assert postRepository.findById(postId).isEmpty();
    }

    @Test
    public void 게시글_좋아요() {
        // given
        Long postId = 1L;
        Long userId = 1L;

        // when
        Long result = postService.toggleLike(postId, userId);

        // then
        assert result == likedRepository.findByPostId(postId).size();
    }
}
