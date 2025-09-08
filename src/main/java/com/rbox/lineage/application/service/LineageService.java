package com.rbox.lineage.application.service;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Service;

import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.lineage.application.port.in.LineageEdge;
import com.rbox.lineage.application.port.in.LineageGraph;
import com.rbox.lineage.application.port.in.LineageNode;
import com.rbox.lineage.application.port.in.LineageUseCase;

/**
 * 간단한 메모리 기반 계보 조회 서비스 구현.
 */
@Service
public class LineageService implements LineageUseCase {

    /** 동물 정보 저장소 (데모용). */
    private final Map<Long, Animal> store = new HashMap<>();

    public LineageService() {
        // 3세대 계보 샘플 데이터 구성
        store.put(123L, new Animal(123L, "Alpha", "F", "GECKO", "2025-08-20", 90L, 91L, 1L));
        store.put(90L, new Animal(90L, "Sire", "M", null, null, 80L, 81L, 0L));
        store.put(91L, new Animal(91L, "Dam", "F", null, null, 82L, 83L, 0L));
        store.put(80L, new Animal(80L, "GF1", "M", null, null, 70L, 71L, 0L));
        store.put(81L, new Animal(81L, "GM1", "F", null, null, 72L, 73L, 0L));
        store.put(82L, new Animal(82L, "GF2", "M", null, null, 74L, 75L, 0L));
        store.put(83L, new Animal(83L, "GM2", "F", null, null, 76L, 77L, 0L));
        store.put(70L, new Animal(70L, "GGF1", "M", null, null, null, null, 0L));
        store.put(71L, new Animal(71L, "GGM1", "F", null, null, null, null, 0L));
        store.put(72L, new Animal(72L, "GGF2", "M", null, null, null, null, 0L));
        store.put(73L, new Animal(73L, "GGM2", "F", null, null, null, null, 0L));
        store.put(74L, new Animal(74L, "GGF3", "M", null, null, null, null, 0L));
        store.put(75L, new Animal(75L, "GGM3", "F", null, null, null, null, 0L));
        store.put(76L, new Animal(76L, "GGF4", "M", null, null, null, null, 0L));
        store.put(77L, new Animal(77L, "GGM4", "F", null, null, null, null, 0L));
    }

    @Override
    public LineageGraph getAncestors(Long uid, Long rootId, int depth) {
        if (depth < 1 || depth > 3) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "depth must be 1..3");
        }
        Animal root = store.get(rootId);
        if (root == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "object not found");
        }
        if (!uid.equals(root.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }

        List<LineageNode> nodes = new ArrayList<>();
        List<LineageEdge> edges = new ArrayList<>();
        Map<Long, Boolean> visited = new HashMap<>();
        Queue<Gen> q = new ArrayDeque<>();
        q.add(new Gen(root, 0));
        visited.put(root.id(), true);

        while (!q.isEmpty()) {
            Gen cur = q.poll();
            Animal a = cur.animal();
            nodes.add(new LineageNode(a.id(), a.name(), a.sexCd(), a.spcCd(), a.bthYmd()));
            if (cur.gen() >= depth) {
                continue;
            }
            if (a.faId() != null) {
                Animal fa = store.get(a.faId());
                if (fa != null) {
                    if (visited.putIfAbsent(fa.id(), true) == null) {
                        q.add(new Gen(fa, cur.gen() + 1));
                    }
                    edges.add(new LineageEdge(fa.id(), a.id(), "FA"));
                }
            }
            if (a.moId() != null) {
                Animal mo = store.get(a.moId());
                if (mo != null) {
                    if (visited.putIfAbsent(mo.id(), true) == null) {
                        q.add(new Gen(mo, cur.gen() + 1));
                    }
                    edges.add(new LineageEdge(mo.id(), a.id(), "MO"));
                }
            }
        }

        Map<String, Object> meta = Map.of("generatedAt", ZonedDateTime.now().toString());
        return new LineageGraph(rootId, depth, nodes, edges, meta);
    }

    /** 내부적으로 사용하는 동물 정보. */
    private record Animal(Long id, String name, String sexCd, String spcCd, String bthYmd, Long faId, Long moId,
            Long ownUsrId) {
    }

    private record Gen(Animal animal, int gen) {
    }
}
