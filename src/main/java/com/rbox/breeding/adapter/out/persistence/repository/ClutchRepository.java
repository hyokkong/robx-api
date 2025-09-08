package com.rbox.breeding.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;

/**
 * 클러치 정보를 저장/조회하기 위한 저장소 인터페이스.
 */
public interface ClutchRepository {
    Long save(ClutchEntity entity);
    Optional<ClutchEntity> findById(Long id);
    List<ClutchEntity> findByFemObjId(Long femObjId);
    List<ClutchEntity> findByMatId(Long matId);
    void update(ClutchEntity entity);
}

