package com.community.backend.service;

import com.community.backend.domain.User;
import com.community.backend.dto.PasswordRequest;
import com.community.backend.dto.UserDTO;
import com.community.backend.dto.UserJoinRequest;
import com.community.backend.dto.UserLoginRequest;
import com.community.backend.repository.UserRepository;
import com.community.backend.util.EmailValidator;
import com.community.backend.util.ImageValidator;
import com.community.backend.util.NicknameValidator;
import com.community.backend.util.PasswordValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void login(UserLoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        // 유효성 검사
        emailValidator.checkEmail(user.getEmail());
        passwordValidator.checkPassword(req.getPassword());
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void join(UserJoinRequest req) {
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
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return new UserDTO(id, user.getNickname(), user.getProfileImgUrl());
    }

    @Override
    public void updateInfo(UserDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 유효성 검사
        nicknameValidator.checkNickname(dto.getNickname());
        imageValidator.checkImage(dto.getProfileImgUrl());

        // 업데이트
        user.setProfileImgUrl(dto.getProfileImgUrl());
        user.setNickname(dto.getNickname());
        userRepository.save(user);
    }

    @Override
    public void updatePassword(PasswordRequest req) {
        // 유효성 검사
        passwordValidator.checkPassword(req.getPassword());

        // 업데이트
        User user = userRepository.findById(req.getId())
                .orElseThrow(EntityNotFoundException::new);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
    }
}
