package com.community.backend.controller;

import com.community.backend.dto.UserLoginRequest;
import com.community.backend.dto.UserSessionDTO;
import com.community.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<?> login(HttpSession session, HttpServletRequest sessionReq, @RequestBody UserLoginRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "로그인 상태에서는 해당 기능을 이용할 수 없습니다.");
        }

        try {
            Long userId = userService.login(req);

            //세션
            UserSessionDTO userSessionDTO = new UserSessionDTO(userId);
            HttpSession newSession = sessionReq.getSession();
            newSession.setAttribute("user", userSessionDTO);

            return ResponseEntity.status(HttpStatus.OK).body("로그인되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage() + "유효성 검사에 실패했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage() + "로그인 과정에 오류가 발생했습니다.");
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
