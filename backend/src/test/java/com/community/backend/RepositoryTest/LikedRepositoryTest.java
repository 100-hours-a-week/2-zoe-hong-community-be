package com.community.backend.RepositoryTest;

import com.community.backend.domain.Liked;
import com.community.backend.domain.LikedId;
import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.domain.enums.UserState;
import com.community.backend.repository.LikedRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LikedRepositoryTest {
    @Autowired LikedRepository likedRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    @Test
    public void countValidLikesByPostId() {
        // given
        User user = makeUser();
        Post post = makePost(user);

        Liked liked = new Liked();
        LikedId likedId = new LikedId(user.getId(), post.getId());
        liked.setId(likedId);
        liked.setUser(user);
        liked.setPost(post);
        likedRepository.save(liked);

        // when
        user.setState(UserState.DELETED);
        Long likedCount = likedRepository.countValidLikesByPostId(post.getId());

        // then
        assert likedCount == 1;
    }

    @Test
    public void findByUserIdAndPostId() {
        // given
        User user = makeUser();
        Post post = makePost(user);

        Liked liked = new Liked();
        LikedId likedId = new LikedId(user.getId(), post.getId());
        liked.setId(likedId);
        liked.setUser(user);
        liked.setPost(post);
        likedRepository.save(liked);

        // when
        Liked resultLiked = likedRepository.findByUserIdAndPostId(user.getId(), post.getId()).orElse(null);

        // then
        assert resultLiked.getId().equals(likedId);
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

    private Post makePost(User user) {
        Post post = new Post();
        post.setTitle("Post Title");
        post.setContent("Post Content");
        post.setImageUrl("imageUrl");
        post.setUser(user);
        postRepository.save(post);

        return post;
    }
}
