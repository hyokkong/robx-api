package com.rbox.object.adapter.out.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 개체 정보를 저장/조회하기 위한 저장소 인터페이스.
 */
@Mapper
public interface ObjectRepository {

    /**
     * 개체를 저장한다.
     *
     * @param entity 저장할 개체
     * @return 생성된 ID
     */
    Long save(ObjectEntity entity);

    /**
     * ID로 개체를 조회한다.
     *
     * @param id 개체 ID
     * @return 개체 정보
     */
    Optional<ObjectEntity> findById(@Param("id") Long id);

    /**
     * 소유자 ID로 개체 목록을 조회한다.
     *
     * @param ownerId 소유자 ID
     * @return 개체 목록
     */
    List<ObjectEntity> findByOwner(@Param("ownerId") Long ownerId);

    /**
     * 개체 정보를 갱신한다.
     */
    void update(ObjectEntity entity);

    /**
     * 개체를 삭제한다.
     */
    void delete(@Param("id") Long id);
}

