package com.rbox.snapshot.application.service;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.lineage.application.port.in.LineageGraph;
import com.rbox.lineage.application.port.in.LineageUseCase;
import com.rbox.snapshot.application.port.in.SnapshotCreateCommand;
import com.rbox.snapshot.application.port.in.SnapshotCreateResp;
import com.rbox.snapshot.application.port.in.SnapshotUseCase;

/**
 * 간단한 메모리 기반 스냅샷 서비스 구현.
 */
@Service
@RequiredArgsConstructor
public class SnapshotService implements SnapshotUseCase {
    private final LineageUseCase lineageUseCase;

    private final Map<String, Snapshot> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1);
    private final SecureRandom random = new SecureRandom();

    @Override
    public SnapshotCreateResp createSnapshot(SnapshotCreateCommand cmd, Long uid) {
        int ttl = cmd.ttlDays() == null ? 30 : cmd.ttlDays();
        if (ttl < 1 || ttl > 365) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "ttlDays must be 1..365");
        }
        LineageGraph graph = lineageUseCase.getAncestors(uid, cmd.rootObjId(), 3);
        String token;
        do {
            token = generateToken();
        } while (store.containsKey(token));
        long id = seq.getAndIncrement();
        ZonedDateTime exp = ZonedDateTime.now().plusDays(ttl);
        store.put(token, new Snapshot(id, cmd.rootObjId(), graph, exp));
        return new SnapshotCreateResp(id, token, exp, cmd.rootObjId());
    }

    @Override
    public LineageGraph getSnapshot(String token) {
        Snapshot snap = store.get(token);
        if (snap == null || (snap.expireAt != null && snap.expireAt.isBefore(ZonedDateTime.now()))) {
            throw new ApiException(ErrorCode.NOT_FOUND, "snapshot not found");
        }
        return snap.payload;
    }

    private String generateToken() {
        byte[] buf = new byte[16];
        random.nextBytes(buf);
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static class Snapshot {
        final long id;
        final long rootObjId;
        final LineageGraph payload;
        final ZonedDateTime expireAt;

        Snapshot(long id, long rootObjId, LineageGraph payload, ZonedDateTime expireAt) {
            this.id = id;
            this.rootObjId = rootObjId;
            this.payload = payload;
            this.expireAt = expireAt;
        }
    }
}
