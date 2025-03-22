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
        Long id = userService.join(req);

        // then
        User findUser = userRepository.findById(id).get();
        assert findUser.getEmail().equals(req.getEmail());
    }

    @Test
    public void 회원탈퇴() {
        // when
        userService.delete(11L);

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
        ProfileRequest req = new ProfileRequest(15L, "nickname1", "changeurl");

        // when
        Long id = userService.updateInfo(req);

        // then
        assert userRepository.findById(15L).get().getNickname().equals(userRepository.findById(id).get().getNickname());
        assert userRepository.findById(15L).get().getEmail().equals(userRepository.findById(id).get().getEmail());
    }

    @Test
    public void 비밀번호수정() {
        // given
        PasswordRequest req = new PasswordRequest(15L, "cC!1word");

        // when
        Long id = userService.updatePassword(req);

        // then
        assert userRepository.findById(15L).get().getEmail().equals(userRepository.findById(id).get().getEmail());
        assert userRepository.findById(15L).get().getPassword().equals(userRepository.findById(id).get().getPassword());
    }
}
