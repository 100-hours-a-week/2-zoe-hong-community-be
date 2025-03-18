package com.community.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="LIKED")
public class Liked {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;
}
