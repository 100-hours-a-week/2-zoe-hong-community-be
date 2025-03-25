package com.community.backend.RepositoryTest;

import com.community.backend.domain.Liked;
import com.community.backend.domain.LikedId;
import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.repository.LikedRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class LikedRepositoryTest {
    @Autowired LikedRepository likedRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    @Test
    public void save() {
        // given
        Liked liked = new Liked();
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        LikedId likedId = new LikedId(user.getId(), post.getId());
        liked.setId(likedId);
        liked.setUser(user);
        liked.setPost(post);

        // when
        likedRepository.save(liked);

        // then
        assert likedRepository.count() == 1;
    }

    @Test
    public void countValidLikesByPostId() {
        // given
        Long postId = 1L;

        // when
        Long likedCount = likedRepository.countValidLikesByPostId(1L);

        // then
        assert likedCount == 3;
    }

    @Test
    public void findByUserIdAndPostId() {
        // when
        Liked liked = likedRepository.findByUserIdAndPostId(1L, 1L).get();

        // then
        assert liked != null;
    }
}
