package com.community.backend;

import com.community.backend.domain.Comment;
import com.community.backend.domain.Post;
import com.community.backend.domain.User;
import com.community.backend.repository.CommentRepository;
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
public class CommentRepositoryTest {
    @Autowired CommentRepository commentRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    @Test
    public void save() {
        // given
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Post not found"));
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
        // when
        List<Comment> comments = commentRepository.findByPostId(1L);

        // then
        assert comments.size() == 1;
    }

    @Test
    public void findAll() {
        // when
        List<Comment> comments = commentRepository.findAll();

        // then
        assert comments.size() == 1;
    }

}
