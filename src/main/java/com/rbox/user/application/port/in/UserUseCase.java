package com.rbox.user.application.port.in;

public interface UserUseCase {
    UserMe getMe(Long uid);
}
