package com.rbox.object.adapter.out.persistence.repository;

import java.util.List;

/**
 * 개체 이미지 저장소 인터페이스.
 */
public interface ObjectImageRepository {

    /**
     * 개체 이미지 정보를 저장한다.
     *
     * @param entity 저장할 이미지
     * @return 생성된 이미지 ID
     */
    Long save(ObjectImageEntity entity);

    /**
     * 개체 ID로 이미지 목록을 조회한다.
     */
    List<ObjectImageEntity> findByObjectId(Long objId);

    /**
     * 이미지 ID로 이미지를 삭제한다.
     */
    void delete(Long imgId);
}

