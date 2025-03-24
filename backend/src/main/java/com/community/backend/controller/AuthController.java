package com.community.backend.controller;

import com.community.backend.common.exception.CustomException;
import com.community.backend.dto.UserLoginRequest;
import com.community.backend.dto.UserSessionDTO;
import com.community.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("login")
    public ResponseEntity<?> login(HttpSession session, HttpServletRequest sessionReq, @RequestBody UserLoginRequest req) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user != null) {
            throw new CustomException(HttpStatus.FORBIDDEN, "이미 로그인된 사용자입니다.");
        }

        try {
            Long userId = userService.login(req);

            //세션
            UserSessionDTO userSessionDTO = new UserSessionDTO(userId);
            HttpSession newSession = sessionReq.getSession();
            newSession.setAttribute("user", userSessionDTO);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "success", true,
                    "id", userId
            ));
        } catch (IllegalArgumentException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpSession session) {
        UserSessionDTO user = (UserSessionDTO) session.getAttribute("user");
        if (user == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 아닙니다.");
        }

        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "success", true,
                "id", user.getUserId()
        ));
    }
}
