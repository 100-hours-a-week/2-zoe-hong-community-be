package com.community.backend;

import com.community.backend.domain.User;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired private UserRepository repository;

    @Test
    public void save() {
        //given
        User user = new User();
        user.setEmail("test1@test.com");
        user.setPassword("password");
        user.setNickname("test1");
        user.setProfileImgUrl("url");

        // when
        repository.save(user);

        // then
        User result = repository.findByEmail(user.getEmail()).orElse(null);
        assert result.getEmail() == user.getEmail();
    }

    @Test
    public void findById() {
        // when
        User user = repository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // then
        assert user.getId() == 1L;
    }

    @Test
    public void findByEmail() {
        // when
        User user = repository.findByEmail("test@test.com").get();

        // then
        assert user.getEmail().equals("test@test.com");
    }

    @Test
    public void findAll() {
        // when
        List<User> result = repository.findAll();

        // then
        assert result.size() == 1;
    }
}
