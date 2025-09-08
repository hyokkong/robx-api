package com.rbox.breeding.adapter.out.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

/**
 * 메모리에 메이팅 정보를 저장하는 구현체.
 */
@Repository
public class InMemoryPairingRepository implements PairingRepository {
    private final Map<Long, PairingEntity> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Long save(PairingEntity entity) {
        Long id = seq.getAndIncrement();
        store.put(id, new PairingEntity(id, entity.femObjId(), entity.malObjId(), entity.matDt(), entity.statCd(), entity.note()));
        return id;
    }

    @Override
    public Optional<PairingEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<PairingEntity> findByFemObjId(Long femObjId) {
        List<PairingEntity> list = new ArrayList<>();
        for (PairingEntity e : store.values()) {
            if (femObjId.equals(e.femObjId())) {
                list.add(e);
            }
        }
        return list;
    }
}

