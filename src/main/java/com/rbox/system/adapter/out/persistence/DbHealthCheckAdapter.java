package com.rbox.system.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.rbox.system.adapter.out.persistence.repository.HealthCheckRepository;
import com.rbox.system.application.port.out.DbHealthCheckOutCommand;
import com.rbox.system.application.port.out.DbHealthCheckPort;

@Component
@RequiredArgsConstructor
public class DbHealthCheckAdapter implements DbHealthCheckPort {
    private final HealthCheckRepository repository;

    @Override
    public boolean check(DbHealthCheckOutCommand command) {
        try {
            Integer result = repository.selectHealth();
            return result != null && result == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
