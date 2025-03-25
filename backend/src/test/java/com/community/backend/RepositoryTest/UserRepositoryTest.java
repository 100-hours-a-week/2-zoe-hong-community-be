package com.community.backend.RepositoryTest;

import com.community.backend.domain.User;
import com.community.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    public void save() {
        //given
        User user = new User();
        user.setEmail("test1@test.com");
        user.setPassword("password");
        user.setNickname("test1");
        user.setProfileImgUrl("url");

        // when
        userRepository.save(user);

        // then
        User result = userRepository.findByEmail(user.getEmail()).orElse(null);
        assert result.getEmail().equals(user.getEmail());
    }

    @Test
    public void findById() {
        // given
        User user = new User();
        user.setEmail("test1@test.com");
        user.setPassword("password");
        user.setNickname("test1");
        user.setProfileImgUrl("url");
        userRepository.save(user);

        // when
        User resultUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // then
        assert resultUser.getId().equals(user.getId());
    }

    @Test
    public void findByEmail() {
        // given
        String email = "test1@test.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setNickname("test1");
        user.setProfileImgUrl("url");
        userRepository.save(user);

        // when
        User resultUser = userRepository.findByEmail(email).get();

        // then
        assert resultUser.getEmail().equals(email);
    }

    @Test
    public void findAll() {
        // given
        int prevCount = userRepository.findAll().size();
        // given
        User user = new User();
        user.setEmail("test1@test.com");
        user.setPassword("password");
        user.setNickname("test1");
        user.setProfileImgUrl("url");
        userRepository.save(user);

        // when
        int presCount = userRepository.findAll().size();

        // then
        assert presCount == prevCount + 1;
    }
}
