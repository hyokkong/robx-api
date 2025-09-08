package com.rbox.snapshot.application.port.in;

import java.time.ZonedDateTime;

/**
 * 스냅샷 생성 결과.
 */
public record SnapshotCreateResp(Long snapId, String shareToken, ZonedDateTime expireAt, Long rootObjId) {}
