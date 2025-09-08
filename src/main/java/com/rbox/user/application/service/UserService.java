package com.rbox.user.application.service;

import org.springframework.stereotype.Service;

import com.rbox.user.application.port.in.UserMe;
import com.rbox.user.application.port.in.UserUseCase;

@Service
public class UserService implements UserUseCase {
    @Override
    public UserMe getMe(Long uid) {
        return new UserMe(uid, "user@example.com", "user", "ACTV");
    }
}
