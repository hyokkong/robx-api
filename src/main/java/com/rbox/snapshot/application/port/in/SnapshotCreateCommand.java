package com.rbox.snapshot.application.port.in;

/**
 * 스냅샷 생성 입력 커맨드.
 */
public record SnapshotCreateCommand(Long rootObjId, Integer ttlDays, String note) {}
