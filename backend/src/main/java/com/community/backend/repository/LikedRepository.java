package com.community.backend.repository;

import com.community.backend.domain.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedRepository extends JpaRepository<Liked, Long> {
    Long countValidLikesByPostId(Long postId);
    Optional<Liked> findByUserIdAndPostId(Long userId, Long postId);

}
