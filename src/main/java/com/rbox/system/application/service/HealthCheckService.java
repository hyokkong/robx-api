package com.rbox.system.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.rbox.system.adapter.in.web.HealthCheckRes;
import com.rbox.system.application.port.in.DbHealthCheckCommand;
import com.rbox.system.application.port.in.HealthCheckUseCase;
import com.rbox.system.application.port.in.ServerHealthCheckCommand;
import com.rbox.system.application.port.out.DbHealthCheckOutCommand;
import com.rbox.system.application.port.out.DbHealthCheckPort;

/**
 * 시스템 헬스 체크 관련 비즈니스 로직을 처리하는 서비스.
 */
@Service
@RequiredArgsConstructor
public class HealthCheckService implements HealthCheckUseCase {
    private final DbHealthCheckPort dbHealthCheckPort;

    /**
     * 서버 상태를 확인한다.
     *
     * @param command 서버 헬스체크 명령
     * @return 서버 상태 결과
     */
    @Override
    public HealthCheckRes checkServerHealth(ServerHealthCheckCommand command) {
        return new HealthCheckRes("OK");
    }

    /**
     * 데이터베이스 연결 상태를 확인한다.
     *
     * @param command DB 헬스체크 명령
     * @return DB 상태 결과
     */
    @Override
    public HealthCheckRes checkDbHealth(DbHealthCheckCommand command) {
        boolean ok = dbHealthCheckPort.check(new DbHealthCheckOutCommand());
        return new HealthCheckRes(ok ? "OK" : "FAIL");
    }
}
