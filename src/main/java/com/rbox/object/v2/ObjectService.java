package com.rbox.object.v2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

/**
 * Very light in-memory service to back the v2 object API.
 */
@Service("objectV2Service")
public class ObjectService {
    private final AtomicLong seq = new AtomicLong(1);
    private final Map<Long, Object> objects = new ConcurrentHashMap<>();

    public Long createObject(String spcCd, String name, String sexCd, String ownerType, Long orgId) {
        long id = seq.getAndIncrement();
        objects.put(id, new Object(id, spcCd, name, sexCd, ownerType, orgId));
        return id;
    }

    public record Object(Long objId, String spcCd, String name, String sexCd, String ownerType, Long orgId) {}
}
