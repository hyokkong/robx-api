package com.rbox.object.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.object.adapter.out.persistence.repository.ObjectEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectImageEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectImageRepository;
import com.rbox.object.adapter.out.persistence.repository.ObjectRepository;
import com.rbox.object.application.port.in.AddImageCommand;
import com.rbox.object.application.port.in.CreateObjectCommand;
import com.rbox.object.application.port.in.ObjectUseCase;
import com.rbox.object.application.port.in.UpdateObjectCommand;

/**
 * 개체 관련 비즈니스 로직을 처리하는 서비스.
 */
@Service("objectV1Service")
@RequiredArgsConstructor
public class ObjectService implements ObjectUseCase {
    private final ObjectRepository repository;
    private final ObjectImageRepository imageRepository;

    @Override
    public Long createObject(CreateObjectCommand command, Long uid) {
        ObjectEntity entity = new ObjectEntity(null, command.spcCd(), command.name(), command.sexCd(),
                command.bthYmd(), "SIMPLE", "ACTV", "N", uid);
        return repository.save(entity);
    }

    @Override
    public List<ObjectEntity> listObjects(Long uid) {
        return repository.findByOwner(uid);
    }

    @Override
    public ObjectEntity getObject(Long id, Long uid) {
        var obj = repository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        if (!uid.equals(obj.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }
        return obj;
    }

    @Override
    public void updateObject(Long id, UpdateObjectCommand command, Long uid) {
        var obj = getObject(id, uid);
        ObjectEntity updated = new ObjectEntity(obj.id(), obj.spcCd(), command.name(), command.sexCd(),
                command.bthYmd(), obj.objMode(), obj.statCd(), obj.marketOk(), obj.ownUsrId());
        repository.update(updated);
    }

    @Override
    public void deleteObject(Long id, Long uid) {
        var obj = getObject(id, uid);
        repository.delete(obj.id());
    }

    @Override
    public List<ObjectImageEntity> listImages(Long objId, Long uid) {
        getObject(objId, uid); // 소유권 확인
        return imageRepository.findByObjectId(objId);
    }

    @Override
    public Long addImage(Long objId, AddImageCommand command, Long uid) {
        getObject(objId, uid); // 소유권 확인
        var images = imageRepository.findByObjectId(objId);
        if (images.size() >= 10) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "max images exceeded");
        }
        return imageRepository.save(new ObjectImageEntity(null, objId, command.url(), command.ordNo()));
    }

    @Override
    public void deleteImage(Long objId, Long imgId, Long uid) {
        getObject(objId, uid); // 소유권 확인
        imageRepository.delete(imgId);
    }
}

