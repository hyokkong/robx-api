package com.rbox.system.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.rbox.system.application.port.in.HealthCheckUseCase;
import com.rbox.system.application.port.in.ServerHealthCheckCommand;
import com.rbox.system.application.port.in.DbHealthCheckCommand;

/**
 * 시스템 헬스 체크 API를 제공하는 Web Controller.
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "시스템 헬스 체크 API")
public class HealthCheckWebCtr {
    private final HealthCheckUseCase useCase;

    /**
     * 서버 상태 확인 API.
     * <p>입력: 없음</p>
     * <p>출력: 서버 상태를 나타내는 {@link HealthCheckRes}</p>
     */
    @GetMapping("/server")
    @Operation(summary = "서버 상태 확인", description = "서버의 상태를 확인합니다")
    public HealthCheckRes server(ServerHealthCheckReq req) {
        return useCase.checkServerHealth(new ServerHealthCheckCommand());
    }

    /**
     * DB 연결 상태 확인 API.
     * <p>입력: 없음</p>
     * <p>출력: DB 상태를 나타내는 {@link HealthCheckRes}</p>
     */
    @GetMapping("/db")
    @Operation(summary = "DB 연결 상태 확인", description = "데이터베이스 연결 상태를 확인합니다")
    public HealthCheckRes db(DbHealthCheckReq req) {
        return useCase.checkDbHealth(new DbHealthCheckCommand());
    }
}
