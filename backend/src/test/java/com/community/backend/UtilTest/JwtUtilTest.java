package com.community.backend.UtilTest;

import com.community.backend.dto.JwtDTO;
import com.community.backend.dto.UserDTO;
import com.community.backend.repository.RefreshTokenRepository;
import com.community.backend.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final Long testUserId = 1L;

    @BeforeEach
    void setUp() {
        String raw = "my-very-secret-key-which-is-long-enough-1234567890";
        String base64 = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));

        JwtDTO dto = new JwtDTO();
        dto.setSecret(base64);
        dto.setExpiration(3600000);      // 1시간
        dto.setRefreshExpiration(86400000); // 1일 = 1000 * 60 * 60 * 24

        jwtUtil = new JwtUtil(dto);
        jwtUtil.init();
    }

    @Test
    void 토큰_생성_파싱() {
        UserDTO user = new UserDTO(testUserId, "testuser", "profile-img-url.jpg");

        String token = jwtUtil.createToken(user);
        Claims claims = jwtUtil.getClaims(token);

        assertEquals("testuser", claims.getSubject());
        assertEquals(1L, claims.get("id", Integer.class).longValue());
    }

    @Test
    void 유효_토큰_검증_성공() {
        UserDTO user = new UserDTO(testUserId, "tester", "url.jpg");
        String token = jwtUtil.createToken(user);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void 유효하지_않은_토큰_검증_실패() {
        String invalidToken = "this.is.not.a.jwt";
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void 리프레시_토큰_생성_저장_조회_검증_삭제() {
        // given
        UserDTO user = new UserDTO(testUserId, "refreshUser", null);
        String refreshToken = jwtUtil.createRefreshToken(user);

        // when: 저장
        refreshTokenRepository.save(testUserId, refreshToken);

        // then: 조회 확인
        Optional<String> found = refreshTokenRepository.find(testUserId);
        assertTrue(found.isPresent());
        assertEquals(refreshToken, found.get());

        // 검증 확인
        assertTrue(refreshTokenRepository.isValid(testUserId, refreshToken));

        // 삭제 후 검증 실패 확인
        refreshTokenRepository.delete(testUserId);
        assertFalse(refreshTokenRepository.find(testUserId).isPresent());
    }
}
