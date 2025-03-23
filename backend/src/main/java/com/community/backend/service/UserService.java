package com.community.backend.service;

import com.community.backend.dto.*;

public interface UserService {
    // 인증
    Long login(UserLoginRequest req);

    // 회원
    String join(UserJoinRequest req);
    void delete(Long userId);
    ProfileResponse getProfile(Long userId);
    String updateProfile(Long userId, ProfileRequest req);
    String updatePassword(Long userId, PasswordRequest req);
}
