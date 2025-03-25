package com.community.backend.RepositoryTest;

import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        User user = makeUser();

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
        int prevCount = postRepository.findAll().size();
        User user = makeUser();
        Post post1 = new Post();
        post1.setTitle("Post Title");
        post1.setContent("Post Content");
        post1.setImageUrl("imageUrl");
        post1.setUser(user);
        postRepository.save(post1);

        // when
        List<Post> posts = postRepository.findAll();

        // then
        System.out.println(posts.size());
        assert posts.size() == prevCount + 1;
    }

    private User makeUser() {
        User user = new User();
        user.setEmail("test1@test.com");
        user.setPassword("password");
        user.setNickname("test1");
        user.setProfileImgUrl("url");

        userRepository.save(user);

        return user;
    }
}
