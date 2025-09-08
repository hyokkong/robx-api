package com.rbox.auth.adapter.in.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.auth.application.port.in.AuthUseCase;
import com.rbox.auth.application.port.in.LoginCommand;
import com.rbox.auth.application.port.in.RefreshCommand;
import com.rbox.auth.application.port.in.TokenResp;
import com.rbox.common.api.ApiResponse;

/**
 * 인증 관련 API를 처리하는 Web Controller.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "인증 관련 API")
public class AuthWebCtr {
    private final AuthUseCase useCase;

    /**
     * 로그인 API.
     * <p>필수 입력: 이메일(email), 비밀번호(password)</p>
     * <p>출력: access/refresh 토큰이 포함된 {@link TokenResp}</p>
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 이용하여 로그인합니다")
    public ApiResponse<TokenResp> login(@Valid @RequestBody LoginReq req) {
        return ApiResponse.success(useCase.login(new LoginCommand(req.email(), req.password())));
    }

    /**
     * 토큰 갱신 API.
     * <p>필수 입력: refreshToken</p>
     * <p>출력: 새로운 access 토큰이 포함된 {@link TokenResp}</p>
     */
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새 액세스 토큰을 발급합니다")
    public ApiResponse<TokenResp> refresh(@Valid @RequestBody RefreshReq req) {
        return ApiResponse.success(useCase.refresh(new RefreshCommand(req.refreshToken())));
    }
}
