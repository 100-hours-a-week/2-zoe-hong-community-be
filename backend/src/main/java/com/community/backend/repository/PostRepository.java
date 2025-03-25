package com.community.backend.repository;

import com.community.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Transactional
    @Modifying
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
    void increaseViewCount(@Param("postId") Long postId);
}
