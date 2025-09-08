package com.rbox.auth.application.service;

import org.springframework.stereotype.Service;

import com.rbox.auth.application.port.in.AuthUseCase;
import com.rbox.auth.application.port.in.LoginCommand;
import com.rbox.auth.application.port.in.RefreshCommand;
import com.rbox.auth.application.port.in.TokenResp;
import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;

@Service
public class AuthService implements AuthUseCase {
    @Override
    public TokenResp login(LoginCommand command) {
        if ("user@example.com".equals(command.email()) &&
                "P@ssw0rd!".equals(command.password())) {
            return new TokenResp("ACCESS_TOKEN", "REFRESH_TOKEN");
        }
        throw new ApiException(ErrorCode.UNAUTHORIZED, "invalid credentials");
    }

    @Override
    public TokenResp refresh(RefreshCommand command) {
        if ("REFRESH_TOKEN".equals(command.refreshToken())) {
            return new TokenResp("NEW_ACCESS_TOKEN", "REFRESH_TOKEN");
        }
        throw new ApiException(ErrorCode.UNAUTHORIZED, "invalid refresh token");
    }
}
