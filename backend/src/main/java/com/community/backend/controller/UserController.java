package com.community.backend.controller;

import com.community.backend.dto.*;
import com.community.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> join(HttpSession session, @RequestBody UserJoinRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 상태에서는 해당 기능을 이용할 수 없습니다.");
        }

        try {
            String username = userService.join(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(username + "님 계정이 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() + "유효성 검사에 실패했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "회원가입 과정에 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("self")
    public ResponseEntity<?> withdraw(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            userService.delete(user.getUserId());
            session.invalidate();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "회원탈퇴 과정에 오류가 발생했습니다.");
        }
    }

    @GetMapping("self/info")
    public ResponseEntity<?> readProfile(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile(user.getUserId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "회원정보를 불러오지 못했습니다.");
        }
    }

    @PatchMapping("self/info")
    public ResponseEntity<?> updateProfile(HttpSession session, @RequestBody ProfileRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            String username = userService.updateProfile(user.getUserId(), req);
            return ResponseEntity.status(HttpStatus.OK).body(username + "님 프로필 수정이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() + "유효성 검사에 실패했습니다.");
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "회원정보 업데이트 과정에 오류가 발생했습니다.");
        }
    }

    @PatchMapping("self/password")
    public ResponseEntity<?> updatePassword(HttpSession session, @RequestBody PasswordRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        try {
            String username = userService.updatePassword(user.getUserId(), req);
            return ResponseEntity.status(HttpStatus.OK).body(username + "님 비밀번호 수정이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() + "유효성 검사에 실패했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "비밀번호 수정 과정에 오류가 발생했습니다.");
        }
    }
}
