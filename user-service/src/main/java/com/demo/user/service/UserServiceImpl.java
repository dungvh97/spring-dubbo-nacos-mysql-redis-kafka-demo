package com.demo.user.service;

import org.apache.dubbo.config.annotation.DubboService;
import com.demo.user.api.UserService;

@DubboService
public class UserServiceImpl implements UserService {

    @Override
    public String getUserById(Long id) {
        return "User_" + id;
    }
}
