package com.example.system.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.system.application.port.in.HealthCheckUseCase;
import com.example.system.application.port.in.ServerHealthCheckCommand;
import com.example.system.application.port.in.DbHealthCheckCommand;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthCheckWebCtr {
    private final HealthCheckUseCase useCase;

    @GetMapping("/server")
    public HealthCheckRes server(ServerHealthCheckReq req) {
        return useCase.checkServerHealth(new ServerHealthCheckCommand());
    }

    @GetMapping("/db")
    public HealthCheckRes db(DbHealthCheckReq req) {
        return useCase.checkDbHealth(new DbHealthCheckCommand());
    }
}
