package com.rbox.object.adapter.out.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

/**
 * 메모리에 개체 정보를 저장하는 테스트용 구현체.
 */
@Repository
public class InMemoryObjectRepository implements ObjectRepository {
    private final Map<Long, ObjectEntity> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Long save(ObjectEntity entity) {
        Long id = seq.getAndIncrement();
        store.put(id, new ObjectEntity(id, entity.spcCd(), entity.name(), entity.sexCd(), entity.bthYmd(),
                entity.objMode(), entity.statCd(), entity.marketOk(), entity.ownUsrId()));
        return id;
    }

    @Override
    public Optional<ObjectEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ObjectEntity> findByOwner(Long ownerId) {
        List<ObjectEntity> list = new ArrayList<>();
        for (ObjectEntity e : store.values()) {
            if (ownerId.equals(e.ownUsrId())) {
                list.add(e);
            }
        }
        return list;
    }

    @Override
    public void update(ObjectEntity entity) {
        store.put(entity.id(), entity);
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}

