package com.rbox.org.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

/**
 * In-memory organization service. This is intentionally simplistic and exists
 * only so that the REST layer has something to interact with.
 */
@Service
public class OrgService {
    private final AtomicLong seq = new AtomicLong(1);
    private final Map<Long, Org> orgs = new ConcurrentHashMap<>();
    private final Map<Long, Map<Long, String>> members = new ConcurrentHashMap<>();

    public Org createOrg(Long ownerId, String orgNm, String orgTp) {
        long id = seq.getAndIncrement();
        Org org = new Org(id, orgNm, orgTp);
        orgs.put(id, org);
        members.computeIfAbsent(id, k -> new ConcurrentHashMap<>()).put(ownerId, "OWNER");
        return org;
    }

    public List<Org> listOrgs(Long userId) {
        List<Org> list = new ArrayList<>();
        members.forEach((orgId, m) -> {
            if (m.containsKey(userId)) {
                list.add(orgs.get(orgId));
            }
        });
        return list;
    }

    public void addMember(Long orgId, Long userId, String roleCd) {
        members.computeIfAbsent(orgId, k -> new ConcurrentHashMap<>()).put(userId, roleCd);
    }

    public void changeMember(Long orgId, Long userId, String roleCd) {
        Map<Long, String> m = members.get(orgId);
        if (m != null) {
            m.put(userId, roleCd);
        }
    }

    public void deleteMember(Long orgId, Long userId) {
        Map<Long, String> m = members.get(orgId);
        if (m != null) {
            m.remove(userId);
        }
    }

    public record Org(Long orgId, String orgNm, String orgTp) {}
}

