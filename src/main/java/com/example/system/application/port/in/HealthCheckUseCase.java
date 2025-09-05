package com.example.system.application.port.in;

import com.example.system.adapter.in.web.HealthCheckRes;

public interface HealthCheckUseCase {
    HealthCheckRes checkServerHealth(ServerHealthCheckCommand command);
    HealthCheckRes checkDbHealth(DbHealthCheckCommand command);
}
