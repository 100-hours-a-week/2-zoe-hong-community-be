package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.PasswordRequest;
import com.community.backend.dto.ProfileRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.dto.UserJoinRequest;
import com.community.backend.service.UserService;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> join(@ModelAttribute UserJoinRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이미 로그인된 사용자입니다.");
        }

        try {
            userService.join(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("self")
    public ResponseEntity<?> withdraw() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = (Long) authentication.getPrincipal();
            userService.delete(userId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "id", userId
            ));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("self/info")
    public ResponseEntity<?> readProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = (Long) authentication.getPrincipal();
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "user", userService.getProfile(userId)
            ));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("self/info")
    public ResponseEntity<?> updateProfile(@ModelAttribute ProfileRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = (Long) authentication.getPrincipal();
            UserDTO res = userService.updateProfile(userId, req);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "profileImgUrl", res.getProfileImgUrl()
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("self/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = (Long) authentication.getPrincipal();
            userService.updatePassword(userId, req);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
