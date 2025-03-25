package com.community.backend.service;

import com.community.backend.dto.*;

public interface UserService {
    // 인증
    UserDTO login(UserLoginRequest req);

    // 회원
    Long join(UserJoinRequest req);
    void delete(Long userId);
    ProfileResponse getProfile(Long userId);
    UserDTO updateProfile(Long userId, ProfileRequest req);
    Long updatePassword(Long userId, PasswordRequest req);
}
