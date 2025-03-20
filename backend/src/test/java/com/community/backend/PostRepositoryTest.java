package com.community.backend;

import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    @Test
    public void save() {
        // given
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle("Post Title");
        post.setContent("Post Content");
        post.setImageUrl("imageUrl");
        post.setUser(user);

        // when
        postRepository.save(post);

        // then
        assert postRepository.findById(post.getId()).get().getTitle().equals(post.getTitle());
    }

    @Test
    public void findAll() {
        // given

        // when
        List<Post> posts = postRepository.findAll();

        // then
        assert posts.size() == 1;
    }
}
