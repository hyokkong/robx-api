package com.rbox.system.application.port.in;

import com.rbox.system.adapter.in.web.HealthCheckRes;

public interface HealthCheckUseCase {
    HealthCheckRes checkServerHealth(ServerHealthCheckCommand command);
    HealthCheckRes checkDbHealth(DbHealthCheckCommand command);
}
