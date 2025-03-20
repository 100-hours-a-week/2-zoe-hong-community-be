package com.community.backend.service;

import com.community.backend.dto.PasswordRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.dto.UserJoinRequest;
import com.community.backend.dto.UserLoginRequest;

public interface UserService {
    // 인증
    public void login(UserLoginRequest req);

    // 회원
    public void join(UserJoinRequest req);
    public void delete(Long id);
    public UserDTO findById(Long id);
    public void updateInfo(UserDTO dto);
    public void updatePassword(PasswordRequest req);
}
