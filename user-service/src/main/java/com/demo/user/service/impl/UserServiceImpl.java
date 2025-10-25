package com.demo.user.service.impl;

import com.demo.user.dto.LoginRequest;
import com.demo.user.dto.RegisterRequest;
import com.demo.user.dto.UserDTO;
import com.demo.user.entity.User;
import com.demo.user.exception.BusinessException;
import com.demo.user.repository.UserRepository;
import com.demo.user.service.UserService;
import com.demo.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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


    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDTO register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BusinessException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());

        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getNickname());
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getId());
    }

    @Override
    public User getUserFromSession(String sessionToken) {
        String userId = redisTemplate.opsForValue().get(SESSION_PREFIX + sessionToken); // todo fix it
        if (userId == null) return null;

        return userRepository.findById(Long.valueOf(userId)).orElse(null);
    }

    @Override
    public void logout(String sessionToken) {
        redisTemplate.delete(SESSION_PREFIX + sessionToken); // todo fix it
    }

    @Override
    @Cacheable(value = "user", key = "#p0")
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getNickname());
    }

    @Override
    @CacheEvict(value = "user", key = "#p0")
    public void updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));

        user.setNickname(userDTO.nickname());
        user.setEmail(userDTO.email());
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#p0")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }
}
