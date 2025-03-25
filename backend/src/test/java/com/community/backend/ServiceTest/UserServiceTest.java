package com.community.backend.ServiceTest;

import com.community.backend.domain.User;
import com.community.backend.domain.enums.UserState;
import com.community.backend.dto.*;
import com.community.backend.repository.UserRepository;
import com.community.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private MockFileGenerator mg;

    @Test
    public void 회원가입() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        UserJoinRequest req = new UserJoinRequest(
                "email1@gmail.com",
                "Pp1!word",
                "nickname1",
                mockFile
        );

        // when
        Long userId = userService.join(req);

        // then
        User findUser = userRepository.findByEmail(req.getEmail()).get();
        assert findUser.getId().equals(userId);
    }

    @Test
    public void 회원탈퇴() {
        //given
        MockMultipartFile mockFile = mg.MockFile();
        UserJoinRequest req = new UserJoinRequest(
                "email1@gmail.com",
                "Pp1!word",
                "nickname1",
                mockFile
        );
        Long id = userService.join(req);

        // when
        userService.delete(id);

        // then
        assert userRepository.findById(id).get().getState().equals(UserState.DELETED);
    }

    @Test
    public void 로그인() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        String email = "email1@gmail.com";
        String password = "aA1!word";
        UserJoinRequest user = new UserJoinRequest(email, password, "nickname1", mockFile);
        Long userId = userService.join(user);


        // when
        UserLoginRequest req = new UserLoginRequest(email, password);
        UserDTO dto = userService.login(req);

        // then
        User findUser = userRepository.findById(dto.getId()).get();
        assert findUser.getEmail().equals(req.getEmail());
    }

    @Test
    public void 회원정보_조회() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        String email = "email1@gmail.com";
        String password = "aA1!word";
        UserJoinRequest user = new UserJoinRequest(email, password, "nickname1", mockFile);
        Long userId = userService.join(user);

        // when
        ProfileResponse res = userService.getProfile(userId);

        // then
        assert userRepository.findById(userId).get().getEmail().equals(res.getEmail());
    }

    @Test
    public void 회원정보_수정() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        String email = "email1@gmail.com";
        String password = "aA1!word";
        String nickname = "nickname1";
        UserJoinRequest user = new UserJoinRequest(email, password, nickname, mockFile);
        Long userId = userService.join(user);

        // when
        String newNickname = "nickname2";
        ProfileRequest req = new ProfileRequest(newNickname, null);
        UserDTO dto = userService.updateProfile(userId, req);

        // then
        assert userRepository.findById(dto.getId()).get().getNickname().equals(dto.getNickname());
    }

    @Test
    public void 비밀번호_수정() {
        // given
        MockMultipartFile mockFile = mg.MockFile();
        String email = "email1@gmail.com";
        String password = "aA1!word";
        String nickname = "nickname1";
        UserJoinRequest user = new UserJoinRequest(email, password, nickname, mockFile);
        Long userId = userService.join(user);

        // when
        String newPassword = "qQ1!word";
        PasswordRequest req = new PasswordRequest(newPassword);
        Long resultId = userService.updatePassword(userId, req);

        // then
        assert userRepository.findById(resultId).get().getId().equals(userId);
    }
}
