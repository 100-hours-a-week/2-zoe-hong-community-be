package com.community.backend.service;

import com.community.backend.domain.User;
import com.community.backend.domain.enums.UserState;
import com.community.backend.dto.*;
import com.community.backend.repository.UserRepository;
import com.community.backend.util.EmailValidator;
import com.community.backend.util.ImageHandler;
import com.community.backend.util.NicknameValidator;
import com.community.backend.util.PasswordValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageHandler imageHandler;

    @Override
    public UserDTO login(UserLoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        // 유효성 검사
        emailValidator.checkEmail(user.getEmail());
        passwordValidator.checkPassword(req.getPassword());
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 탈퇴 여부
        if (user.getState() == UserState.DELETED) {
            throw new IllegalStateException("이미 탈퇴한 사용자입니다.");
        }

        return new UserDTO(
                user.getId(),
                user.getNickname(),
                user.getProfileImgUrl()
        );
    }

    @Override
    public Long join(UserJoinRequest req) {
        // 유효성 검사
        MultipartFile profileImage = req.getProfileImg();
        emailValidator.checkEmail(req.getEmail());
        passwordValidator.checkPassword(req.getPassword());
        nicknameValidator.checkNickname(req.getNickname());

        // 회원가입
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        user.setProfileImgUrl(imageHandler.saveImage(profileImage));
        userRepository.save(user);

        return user.getId();
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        if (user.getState() == UserState.DELETED) {
            throw new RuntimeException("이미 탈퇴한 회원입니다.");
        }

        user.setState(UserState.DELETED);
        user.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
    }

    @Override
    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        return new ProfileResponse(user.getEmail(), user.getNickname(), user.getProfileImgUrl());
    }

    @Override
    public UserDTO updateProfile(Long userId, ProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        // 유효성 검사
        if (!user.getNickname().equals(req.getNickname())) {
            nicknameValidator.checkNickname(req.getNickname());
        }

        // 업데이트
        user.setNickname(req.getNickname());
        System.out.println(req.getProfileImg());
        if (req.getProfileImg() != null) {
            user.setProfileImgUrl(imageHandler.saveImage(req.getProfileImg()));
        }
        userRepository.save(user);

        return new UserDTO(
          user.getId(),
          user.getNickname(),
          user.getProfileImgUrl()
        );
    }

    @Override
    public Long updatePassword(Long userId, PasswordRequest req) {
        // 유효성 검사
        passwordValidator.checkPassword(req.getPassword());

        // 업데이트
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        return user.getId();
    }
}
