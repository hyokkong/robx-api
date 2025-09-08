package com.rbox.breeding.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 클러치 정보를 저장/조회하기 위한 저장소 인터페이스.
 */
@Mapper
public interface ClutchRepository {
    Long save(ClutchEntity entity);
    Optional<ClutchEntity> findById(@Param("id") Long id);
    List<ClutchEntity> findByFemObjId(@Param("femObjId") Long femObjId);
    List<ClutchEntity> findByMatId(@Param("matId") Long matId);
    void update(ClutchEntity entity);
}

