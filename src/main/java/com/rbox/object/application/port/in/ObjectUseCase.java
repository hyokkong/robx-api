package com.rbox.object.application.port.in;

import java.util.List;

import com.rbox.object.adapter.out.persistence.repository.ObjectEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectImageEntity;

/**
 * 개체 도메인의 UseCase 인터페이스.
 */
public interface ObjectUseCase {

    Long createObject(CreateObjectCommand command, Long uid);

    List<ObjectEntity> listObjects(Long uid);

    ObjectEntity getObject(Long id, Long uid);

    void updateObject(Long id, UpdateObjectCommand command, Long uid);

    void deleteObject(Long id, Long uid);

    List<ObjectImageEntity> listImages(Long objId, Long uid);

    Long addImage(Long objId, AddImageCommand command, Long uid);

    void deleteImage(Long objId, Long imgId, Long uid);
}

