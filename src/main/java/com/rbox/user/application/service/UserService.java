package com.rbox.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.user.adapter.out.persistence.repository.UserRepository;
import com.rbox.user.application.port.in.UserMe;
import com.rbox.user.application.port.in.UserUseCase;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private final UserRepository repository;

    /**
     * 로그인한 사용자의 정보를 조회한다.
     *
     * @param uid 사용자 ID
     * @return 사용자 기본 정보
     */
    @Override
    public UserMe getMe(Long uid) {
        var user = repository.findById(uid);
        if (user == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "user not found");
        }
        return new UserMe(user.id(), user.email(), user.nick(), user.stat());
    }
}
