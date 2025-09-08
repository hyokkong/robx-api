package com.rbox.snapshot.adapter.in.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 스냅샷 생성 요청.
 */
public record SnapshotCreateReq(
        @NotNull Long rootObjId,
        @Min(1) @Max(365) Integer ttlDays,
        String note
) {}
