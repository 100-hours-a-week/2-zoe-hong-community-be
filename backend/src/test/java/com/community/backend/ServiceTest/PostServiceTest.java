package com.community.backend.ServiceTest;

import com.community.backend.dto.*;
import com.community.backend.repository.LikedRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.service.PostService;
import com.community.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired private PostService postService;
    @Autowired private PostRepository postRepository;
    @Autowired private LikedRepository likedRepository;
    @Autowired private UserService userService;
    @Autowired private MockFileGenerator mg;

    @Test
    public void 게시글_목록_조회() {
        //given
        int prevCount = postService.getPostList().size();

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
        UserDTO user = userService.login(loginReq);

        // when
        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);
        int presCount = postService.getPostList().size();

        // then
        assert presCount == prevCount + 1;
    }

    @Test
    public void 게시글_생성_조회() {
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
        UserDTO user = userService.login(loginReq);

        // when
        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        // then
        PostDTO dto = postService.getPostById(postId);
        assert postRepository.findById(postId).get().getTitle().equals(dto.getTitle());
    }

    @Test
    public void 게시글_수정() {
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
        UserDTO user = userService.login(loginReq);

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        // when
        PostRequest postUpdateReq = new PostRequest(
                "new title",
                "new content",
                null
        );
        Long resultId = postService.update(userId, postId, postUpdateReq);

        // then
        assert postRepository.findById(resultId).get().getTitle().equals(postUpdateReq.getTitle());
    }

    @Test
    public void 게시글_삭제() {
        /// given
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
        UserDTO user = userService.login(loginReq);

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        // when
        postService.delete(userId, postId);

        // then
        assert postRepository.findById(postId).isEmpty();
    }

    @Test
    public void 게시글_좋아요() {
        // given
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
        UserDTO user = userService.login(loginReq);

        PostRequest postReq = new PostRequest(
                "title",
                "content",
                null
        );
        Long postId = postService.save(userId, postReq);

        // when
        Long result = postService.toggleLike(userId, postId);

        // then
        assert postService.isLiked(userId, postId);
    }
}
