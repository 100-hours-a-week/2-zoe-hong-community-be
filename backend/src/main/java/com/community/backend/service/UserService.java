package com.community.backend.service;

import com.community.backend.dto.PasswordRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.dto.UserJoinRequest;
import com.community.backend.dto.UserLoginRequest;

public interface UserService {
    // 인증
    public void login(UserLoginRequest req);

    // 회원
    void join(UserJoinRequest req);
    void delete(Long id);
    UserDTO findById(Long id);
    void updateInfo(UserDTO dto);
    void updatePassword(PasswordRequest req);
}
