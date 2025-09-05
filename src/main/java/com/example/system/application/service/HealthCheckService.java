package com.example.system.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.system.adapter.in.web.HealthCheckRes;
import com.example.system.application.port.in.DbHealthCheckCommand;
import com.example.system.application.port.in.HealthCheckUseCase;
import com.example.system.application.port.in.ServerHealthCheckCommand;
import com.example.system.application.port.out.DbHealthCheckOutCommand;
import com.example.system.application.port.out.DbHealthCheckPort;

@Service
@RequiredArgsConstructor
public class HealthCheckService implements HealthCheckUseCase {
    private final DbHealthCheckPort dbHealthCheckPort;

    @Override
    public HealthCheckRes checkServerHealth(ServerHealthCheckCommand command) {
        return new HealthCheckRes("OK");
    }

    @Override
    public HealthCheckRes checkDbHealth(DbHealthCheckCommand command) {
        boolean ok = dbHealthCheckPort.check(new DbHealthCheckOutCommand());
        return new HealthCheckRes(ok ? "OK" : "FAIL");
    }
}
