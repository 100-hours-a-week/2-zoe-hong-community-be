package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.ProfileResponse;
import com.community.backend.dto.RefreshRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.dto.UserLoginRequest;
import com.community.backend.repository.RefreshTokenRepository;
import com.community.backend.security.JwtUtil;
import com.community.backend.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이미 로그인된 사용자입니다.");
        }

        try {
            UserDTO res = userService.login(req);

            // JWT
            String accessToken = jwtUtil.createToken(res);
            String refreshToken = jwtUtil.createRefreshToken(res);

            refreshTokenRepository.save(res.getId(), refreshToken);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "token", accessToken,
                    "refreshToken", refreshToken,
                    "profileImgUrl", res.getProfileImgUrl()
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.OK, "로그인된 사용자가 아닙니다.");
        }

        String token = authHeader.replace("Bearer ", "").trim();
        Claims claims = jwtUtil.getClaims(token);
        Long userId = claims.get("id", Long.class);

        refreshTokenRepository.delete(userId);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "success", true,
                "id", userId
        ));
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            }

            Claims claims = jwtUtil.getClaims(refreshToken);
            Long userId = claims.get("id", Long.class);

            if (!refreshTokenRepository.isValid(userId, refreshToken)) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "토큰이 불일치하거나 만료되었습니다.");
            }

            ProfileResponse profile = userService.getProfile(userId);
            UserDTO newUser = new UserDTO(
                    userId,
                    profile.getNickname(),
                    profile.getProfileImg()
            );

            String newAccessToken = jwtUtil.createToken(newUser);
            return ResponseEntity.ok(Map.of("token", newAccessToken));
        } catch (RuntimeException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
