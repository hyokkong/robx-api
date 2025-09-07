package com.example.system.application.port.out;

public interface DbHealthCheckPort {
    boolean check(DbHealthCheckOutCommand command);
}
