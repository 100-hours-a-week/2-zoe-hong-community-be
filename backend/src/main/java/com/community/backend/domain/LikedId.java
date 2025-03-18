package com.community.backend.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class LikedId implements Serializable {
    // 복합 키를 PK로 사용하기 위해 직렬화
    private Long user;
    private Long post;
}
