package com.demo.user.service;

import com.demo.user.entity.User;

public interface UserService {

    User register(String username, String email, String password);

    User login(String username, String password);

    User getUserFromSession(String sessionToken);

    void logout(String sessionToken);
}

