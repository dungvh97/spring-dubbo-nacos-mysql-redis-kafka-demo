package com.demo.user.service;

import com.demo.user.entity.User;
import com.demo.user.dto.UserDTO;
import com.demo.user.dto.LoginRequest;
import com.demo.user.dto.RegisterRequest;

public interface UserService {

    UserDTO register(RegisterRequest request);

    String login(LoginRequest request);

    User getUserFromSession(String sessionToken);

    void logout(String sessionToken);

    UserDTO getUserById(Long id);

    void updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}

