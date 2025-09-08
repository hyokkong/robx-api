package com.rbox.user.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.user.application.port.in.UserMe;
import com.rbox.user.application.port.in.UserUseCase;

/**
 * 사용자 관련 API를 제공하는 Web Controller.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserWebCtr {
    private final UserUseCase useCase;

    /**
     * 내 정보 조회 API.
     * <p>입력: 없음(인증 정보 이용)</p>
     * <p>출력: 사용자 기본 정보 {@link UserMe}</p>
     */
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "인증 정보를 이용하여 내 정보를 조회합니다")
    public ApiResponse<UserMe> me() {
        return ApiResponse.success(useCase.getMe(1L));
    }
}
