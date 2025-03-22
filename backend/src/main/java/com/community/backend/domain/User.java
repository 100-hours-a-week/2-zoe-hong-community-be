package com.community.backend.domain;

import com.community.backend.domain.enums.UserRole;
import com.community.backend.domain.enums.UserState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 기능 -> createdAt 자동 관리
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "profile_img_url", nullable = false)
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserState state = UserState.ACTIVE;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
}
