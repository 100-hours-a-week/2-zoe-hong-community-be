package com.community.backend.RepositoryTest;

import com.community.backend.domain.Comment;
import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.domain.enums.UserState;
import com.community.backend.repository.CommentRepository;
import com.community.backend.repository.PostRepository;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {
    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    @Test
    public void save() {
        // given
        User user = makeUser();
        Post post = makePost(user);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("Comment Content");

        // when
        commentRepository.save(comment);

        // then
        assert commentRepository.findById(comment.getId()).get().getContent().equals(comment.getContent());
    }

    @Test
    public void findByPostId() {
        // given
        User user = makeUser();
        Post post = makePost(user);

        int prevCount = commentRepository.findByPostId(post.getId()).size();

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("Comment Content");
        commentRepository.save(comment);

        // when
        int presCount = commentRepository.findByPostId(post.getId()).size();

        // then
        assert presCount == prevCount + 1;
    }

    @Test
    public void countValidCommentsByPostId() {
        // given
        User user = makeUser();
        Post post = makePost(user);

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent("Comment Content");
        commentRepository.save(comment);

        // when
        user.setState(UserState.DELETED);
        Long count = commentRepository.countValidCommentsByPostId(post.getId());

        // then
        assert count == 0;

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
