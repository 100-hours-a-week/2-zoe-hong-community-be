package com.community.backend.domain;

import com.community.backend.domain.enums.UserRole;
import com.community.backend.domain.enums.UserState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="USERS")
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 기능 -> createdAt 자동 관리
@SQLDelete(sql="UPDATE USERS SET deleted_at = NOW() WHERE id = ?") // Soft Delete
@Where(clause = "deleted_at IS NULL") // Soft Delete
public class Users {
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
    @SQLDelete(sql = "UPDATE USERS SET deleted_at = NOW() WHERE id = ?")  // 삭제 시 deleted_at 설정
    @Where(clause = "deleted_at IS NULL")  // deleted_at이 NULL인 데이터만 조회
    private Timestamp deletedAt;
}
