package com.rbox.auth.application.port.in;

public interface AuthUseCase {
    TokenResp login(LoginCommand command);
    TokenResp refresh(RefreshCommand command);
}
