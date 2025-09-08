package com.rbox.object.adapter.out.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

/**
 * 메모리에 개체 이미지를 저장하는 테스트용 구현체.
 */
@Repository
public class InMemoryObjectImageRepository implements ObjectImageRepository {
    private final Map<Long, List<ObjectImageEntity>> store = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Long save(ObjectImageEntity entity) {
        Long id = seq.getAndIncrement();
        ObjectImageEntity saved = new ObjectImageEntity(id, entity.objId(), entity.url(), entity.ordNo());
        store.computeIfAbsent(entity.objId(), k -> new ArrayList<>()).add(saved);
        return id;
    }

    @Override
    public List<ObjectImageEntity> findByObjectId(Long objId) {
        return store.getOrDefault(objId, new ArrayList<>());
    }

    @Override
    public void delete(Long imgId) {
        for (List<ObjectImageEntity> list : store.values()) {
            list.removeIf(img -> img.imgId().equals(imgId));
        }
    }
}

