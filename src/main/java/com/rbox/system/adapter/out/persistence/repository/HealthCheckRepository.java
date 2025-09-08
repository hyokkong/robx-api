package com.rbox.system.adapter.out.persistence.repository;

import org.apache.ibatis.annotations.Mapper;

/**
 * 시스템 DB 헬스 체크를 위한 쿼리를 정의하는 MyBatis Mapper.
 */
@Mapper
public interface HealthCheckRepository {
    /**
     * DB 연결을 확인하기 위한 간단한 쿼리.
     *
     * @return 항상 1을 반환하여 DB 연결 여부를 확인
     */
    Integer selectHealth();
}
