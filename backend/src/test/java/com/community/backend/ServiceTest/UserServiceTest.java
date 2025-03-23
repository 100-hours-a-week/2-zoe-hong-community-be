package com.community.backend.ServiceTest;

import com.community.backend.domain.User;
import com.community.backend.domain.enums.UserState;
import com.community.backend.dto.*;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    @Test
    public void 회원가입() {
        // given
        UserJoinRequest req = new UserJoinRequest(
                "email1@gmail.com",
                "Pp1!word",
                "nickname1",
                "url"
        );

        // when
        String username = userService.join(req);

        // then
        User findUser = userRepository.findByEmail(req.getEmail()).get();
        assert findUser.getNickname().equals(username);
    }

    @Test
    public void 회원탈퇴() {
        // when
        userService.delete(15L);

        // then
        assert userRepository.findById(11L).get().getState().equals(UserState.DELETED);
    }

    @Test
    public void 로그인() {
        // given
        UserLoginRequest req = new UserLoginRequest(
                "email@gmail.com",
                "Pp1!word"
        );

        // when
        Long id = userService.login(req);

        // then
        User findUser = userRepository.findById(id).get();
        assert findUser.getEmail().equals(req.getEmail());
    }

    @Test
    public void 회원정보조회() {
        // when
        ProfileResponse res = userService.getProfile(15L);

        // then
        assert userRepository.findById(15L).get().getEmail().equals(res.getEmail());
    }

    @Test
    public void 회원정보수정() {
        // given
        Long userId = 1L;
        ProfileRequest req = new ProfileRequest("nickname1", "changeurl");

        // when
        String username = userService.updateProfile(userId, req);

        // then
        assert userRepository.findById(15L).get().getNickname().equals(username);
    }

    @Test
    public void 비밀번호수정() {
        // given
        Long userId = 15L;
        PasswordRequest req = new PasswordRequest("cC!1word");

        // when
        String username = userService.updatePassword(userId, req);

        // then
        assert userRepository.findById(15L).get().getNickname().equals(username);
    }
}
