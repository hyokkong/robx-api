package com.rbox.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rbox.auth.application.port.in.AuthUseCase;
import com.rbox.auth.application.port.in.LoginCommand;
import com.rbox.auth.application.port.in.RefreshCommand;
import com.rbox.auth.application.port.in.TokenResp;
import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.user.adapter.out.persistence.repository.UserRepository;

/**
 * 인증 도메인 로직을 처리하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    private final UserRepository repository;
    private final JwtTokenProvider tokenProvider;

    /**
     * 사용자의 로그인 요청을 처리한다.
     *
     * @param command 로그인 정보(email, password)
     * @return 발급된 토큰 응답
     */
    @Override
    public TokenResp login(LoginCommand command) {
        var user = repository.findByEmail(command.email());
        if (user != null && command.password().equals(user.password())) {
            String accessToken = tokenProvider.generateAccessToken(user);
            String refreshToken = tokenProvider.generateRefreshToken(user);
            return new TokenResp(accessToken, refreshToken);
        }
        throw new ApiException(ErrorCode.UNAUTHORIZED, "invalid credentials");
    }

    /**
     * refresh 토큰으로 access 토큰을 재발급한다.
     *
     * @param command refresh 토큰 정보
     * @return 새롭게 발급된 토큰 응답
     */
    @Override
    public TokenResp refresh(RefreshCommand command) {
        if (!tokenProvider.validateToken(command.refreshToken())) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "invalid refresh token");
        }
        var claims = tokenProvider.getClaims(command.refreshToken());
        Long userId = Long.parseLong(claims.getSubject());
        var user = repository.findById(userId);
        if (user == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, "invalid refresh token");
        }
        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);
        return new TokenResp(accessToken, refreshToken);
    }
}
