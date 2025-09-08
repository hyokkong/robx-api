package com.rbox.breeding.adapter.out.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

/**
 * 메모리에 클러치 정보를 저장하는 구현체.
 */
@Repository
public class InMemoryClutchRepository implements ClutchRepository {
    private final Map<Long, ClutchEntity> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Long save(ClutchEntity entity) {
        Long id = seq.getAndIncrement();
        store.put(id, new ClutchEntity(id, entity.matId(), entity.femObjId(), entity.cltNo(), entity.layYmd(),
                entity.eggCount(), entity.statCd(), entity.chkYn()));
        return id;
    }

    @Override
    public Optional<ClutchEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ClutchEntity> findByFemObjId(Long femObjId) {
        List<ClutchEntity> list = new ArrayList<>();
        for (ClutchEntity e : store.values()) {
            if (femObjId.equals(e.femObjId())) {
                list.add(e);
            }
        }
        return list;
    }

    @Override
    public List<ClutchEntity> findByMatId(Long matId) {
        List<ClutchEntity> list = new ArrayList<>();
        for (ClutchEntity e : store.values()) {
            if (matId.equals(e.matId())) {
                list.add(e);
            }
        }
        return list;
    }

    @Override
    public void update(ClutchEntity entity) {
        store.put(entity.id(), entity);
    }
}

