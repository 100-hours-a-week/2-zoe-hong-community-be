package com.community.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="LIKED")
public class Liked {
    @EmbeddedId
    private LikedId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user")
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("post")
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;
}
