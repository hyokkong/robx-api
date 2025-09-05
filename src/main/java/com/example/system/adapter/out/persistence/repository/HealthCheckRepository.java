package com.example.system.adapter.out.persistence.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthCheckRepository {
    Integer selectHealth();
}
