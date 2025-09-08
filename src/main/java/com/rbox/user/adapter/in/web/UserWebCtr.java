package com.rbox.user.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.user.application.port.in.UserMe;
import com.rbox.user.application.port.in.UserUseCase;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserWebCtr {
    private final UserUseCase useCase;

    @GetMapping("/me")
    public ApiResponse<UserMe> me() {
        return ApiResponse.success(useCase.getMe(1L));
    }
}
