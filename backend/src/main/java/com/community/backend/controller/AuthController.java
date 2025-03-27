package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.UserDTO;
import com.community.backend.dto.UserLoginRequest;
import com.community.backend.security.JwtUtil;
import com.community.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이미 로그인된 사용자입니다.");
        }

        try {
            UserDTO res = userService.login(req);

            // JWT
            String token = jwtUtil.createToken(res);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "token", token,
                    "profileImgUrl", res.getProfileImgUrl()
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.OK, "로그인된 사용자가 아닙니다.");
        }

        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "success", true,
                "id", userId
        ));
    }
}
