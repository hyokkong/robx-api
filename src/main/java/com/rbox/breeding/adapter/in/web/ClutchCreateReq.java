package com.rbox.breeding.adapter.in.web;

import jakarta.validation.constraints.NotNull;

/**
 * 클러치 생성 요청.
 */
public record ClutchCreateReq(
        @NotNull Long matId,
        @NotNull Long femObjId,
        Integer cltNo,
        String layYmd,
        @NotNull Integer eggCount
) {}

