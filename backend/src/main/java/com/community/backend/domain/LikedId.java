package com.community.backend.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
public class LikedId implements Serializable {
    // 복합 키를 PK로 사용하기 위해 직렬화
    private Long userId;
    private Long postId;

    protected LikedId() {}
    public LikedId(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
