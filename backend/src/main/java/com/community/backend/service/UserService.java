package com.community.backend.service;

import com.community.backend.dto.*;

public interface UserService {
    // 인증
    void login(UserLoginRequest req);

    // 회원
    void join(UserJoinRequest req);
    void delete(Long id);
    UserDTO findById(Long id);
    ProfileResponse getProfile(Long id);
    void updateInfo(UserDTO dto);
    void updatePassword(PasswordRequest req);
}
