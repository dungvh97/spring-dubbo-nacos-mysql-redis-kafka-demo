package com.demo.user.service;

import org.apache.dubbo.config.annotation.DubboService;
import com.demo.user.api.UserService;

@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public String getUserById(Long id) {
        return "User_" + id;
    }
}
