package com.rbox.breeding.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 메이팅 정보를 저장/조회하기 위한 저장소 인터페이스.
 */
@Mapper
public interface PairingRepository {
    Long save(PairingEntity entity);
    Optional<PairingEntity> findById(@Param("id") Long id);
    List<PairingEntity> findByFemObjId(@Param("femObjId") Long femObjId);
}

