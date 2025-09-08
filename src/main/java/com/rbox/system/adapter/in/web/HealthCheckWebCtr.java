package com.rbox.system.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbox.system.application.port.in.HealthCheckUseCase;
import com.rbox.system.application.port.in.ServerHealthCheckCommand;
import com.rbox.system.application.port.in.DbHealthCheckCommand;

/**
 * 시스템 헬스 체크 API를 제공하는 Web Controller.
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthCheckWebCtr {
    private final HealthCheckUseCase useCase;

    /**
     * 서버 상태 확인 API.
     * <p>입력: 없음</p>
     * <p>출력: 서버 상태를 나타내는 {@link HealthCheckRes}</p>
     */
    @GetMapping("/server")
    public HealthCheckRes server(ServerHealthCheckReq req) {
        return useCase.checkServerHealth(new ServerHealthCheckCommand());
    }

    /**
     * DB 연결 상태 확인 API.
     * <p>입력: 없음</p>
     * <p>출력: DB 상태를 나타내는 {@link HealthCheckRes}</p>
     */
    @GetMapping("/db")
    public HealthCheckRes db(DbHealthCheckReq req) {
        return useCase.checkDbHealth(new DbHealthCheckCommand());
    }
}
