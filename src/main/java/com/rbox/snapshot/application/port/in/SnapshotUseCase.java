package com.rbox.snapshot.application.port.in;

import com.rbox.lineage.application.port.in.LineageGraph;

/**
 * 3세대 계보 스냅샷 관련 유스케이스.
 */
public interface SnapshotUseCase {

    /**
     * 스냅샷을 생성한다.
     */
    SnapshotCreateResp createSnapshot(SnapshotCreateCommand cmd, Long uid);

    /**
     * 토큰으로 스냅샷을 조회한다.
     */
    LineageGraph getSnapshot(String token);
}
