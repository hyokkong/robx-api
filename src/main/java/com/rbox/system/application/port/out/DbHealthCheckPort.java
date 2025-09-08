package com.rbox.system.application.port.out;

public interface DbHealthCheckPort {
    boolean check(DbHealthCheckOutCommand command);
}
