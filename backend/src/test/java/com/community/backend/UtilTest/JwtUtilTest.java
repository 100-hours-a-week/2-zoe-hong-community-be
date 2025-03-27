package com.community.backend.UtilTest;

import com.community.backend.dto.JwtDTO;
import com.community.backend.dto.UserDTO;
import com.community.backend.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String raw = "my-very-secret-key-which-is-long-enough-1234567890";
        String base64 = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));

        JwtDTO dto = new JwtDTO();
        dto.setSecret(base64);
        dto.setExpiration(3600000);

        jwtUtil = new JwtUtil(dto);
        jwtUtil.init();
    }

    @Test
    void 토큰_생성_파싱() {
        // given
        UserDTO user = new UserDTO(
                1L,
                "testuser",
                "profile-img-url.jpg"
        );

        // when
        String token = jwtUtil.createToken(user);
        Claims claims = jwtUtil.getClaims(token);

        // then
        assert claims.getSubject().equals("testuser");
        assert claims.get("id", Long.class).equals(1L);
        assert claims.get("profileImgUrl", String.class).equals("profile-img-url.jpg");
    }

    @Test
    void 유효_토큰_검증_성공() {
        UserDTO user = new UserDTO(
                1L,
                "tester",
                "url.jpg"
        );

        String token = jwtUtil.createToken(user);
        assert jwtUtil.validateToken(token);
    }

    @Test
    void 유효하지_않은_토큰_검증_실패() {
        String invalidToken = "this.is.not.a.jwt";
        assert !jwtUtil.validateToken(invalidToken);
    }
}
