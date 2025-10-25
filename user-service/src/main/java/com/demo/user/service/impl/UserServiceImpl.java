package com.demo.user.service.impl;

import com.demo.user.entity.User;
import com.demo.user.repository.UserRepository;
import com.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@DubboService(version = "1.0.0")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String SESSION_PREFIX = "session:";

    @Override
    public User register(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // generate session token
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(SESSION_PREFIX + token, user.getId().toString(), Duration.ofHours(2));

        // attach temporary token
        user.setPassword(token);
        return user;
    }

    @Override
    public User getUserFromSession(String sessionToken) {
        String userId = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionToken);
        if (userId == null) return null;

        return userRepository.findById(Long.valueOf(userId)).orElse(null);
    }

    @Override
    public void logout(String sessionToken) {
        redisTemplate.delete(SESSION_PREFIX + sessionToken);
    }
}
