package com.community.backend.repository;

import com.community.backend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.user.state <> com.community.backend.domain.enums.UserState.DELETED")
    Long countValidCommentsByPostId(@Param("postId") Long postId);
}
