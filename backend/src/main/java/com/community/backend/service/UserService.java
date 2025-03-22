package com.community.backend.service;

import com.community.backend.dto.*;

public interface UserService {
    // 인증
    Long login(UserLoginRequest req);

    // 회원
    Long join(UserJoinRequest req);
    void delete(Long id);
    ProfileResponse getProfile(Long id);
    Long updateProfile(ProfileRequest req);
    Long updatePassword(PasswordRequest req);
}
