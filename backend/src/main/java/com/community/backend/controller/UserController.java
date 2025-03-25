package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.*;
import com.community.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> join(HttpSession session, @ModelAttribute UserJoinRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user != null) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이미 로그인된 사용자입니다.");
        }

        try {
            Long id = userService.join(req);
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
    public ResponseEntity<?> withdraw(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            userService.delete(user.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "id", user.getUserId()
            ));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            session.invalidate();
        }
    }

    @GetMapping("self/info")
    public ResponseEntity<?> readProfile(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "user", userService.getProfile(user.getUserId())
            ));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("self/info")
    public ResponseEntity<?> updateProfile(HttpSession session, @ModelAttribute ProfileRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            UserDTO res = userService.updateProfile(user.getUserId(), req);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "user", res
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("self/password")
    public ResponseEntity<?> updatePassword(HttpSession session, @RequestBody PasswordRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            Long userId = userService.updatePassword(user.getUserId(), req);
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
