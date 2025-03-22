package com.community.backend.service;

import com.community.backend.domain.User;
import com.community.backend.domain.enums.UserState;
import com.community.backend.dto.*;
import com.community.backend.repository.UserRepository;
import com.community.backend.util.EmailValidator;
import com.community.backend.util.ImageValidator;
import com.community.backend.util.NicknameValidator;
import com.community.backend.util.PasswordValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final NicknameValidator nicknameValidator;
    private final ImageValidator imageValidator;

    @Override
    public Long login(UserLoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        // 유효성 검사
        emailValidator.checkEmail(user.getEmail());
        passwordValidator.checkPassword(req.getPassword());
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 탈퇴 여부
        if (user.getDeletedAt() != null) {
            throw new IllegalStateException("이미 탈퇴한 사용자입니다.");
        }

        return user.getId();
    }

    @Override
    public Long join(UserJoinRequest req) {
        // 유효성 검사
        imageValidator.checkImage(req.getProfileImgUrl());
        emailValidator.checkEmail(req.getEmail());
        passwordValidator.checkPassword(req.getPassword());
        nicknameValidator.checkNickname(req.getNickname());

        // 회원가입
        User user = new User();
        user.setProfileImgUrl(req.getProfileImgUrl());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        user.setState(UserState.DELETED);
        user.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
    }

    @Override
    public ProfileResponse getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return new ProfileResponse(user.getEmail(), user.getNickname(), user.getProfileImgUrl());
    }

    @Override
    public Long updateInfo(ProfileRequest req) {
        User user = userRepository.findById(req.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 유효성 검사
        nicknameValidator.checkNickname(req.getNickname());
        imageValidator.checkImage(req.getProfileImgUrl());

        // 업데이트
        user.setProfileImgUrl(req.getProfileImgUrl());
        user.setNickname(req.getNickname());
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public Long updatePassword(PasswordRequest req) {
        // 유효성 검사
        passwordValidator.checkPassword(req.getPassword());

        // 업데이트
        User user = userRepository.findById(req.getId())
                .orElseThrow(EntityNotFoundException::new);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        return user.getId();
    }
}
