package com.rbox.breeding.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;

/**
 * 메이팅 정보를 저장/조회하기 위한 저장소 인터페이스.
 */
public interface PairingRepository {
    Long save(PairingEntity entity);
    Optional<PairingEntity> findById(Long id);
    List<PairingEntity> findByFemObjId(Long femObjId);
}

