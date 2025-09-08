package com.rbox.breeding.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 메이팅 생성 요청.
 */
public record PairingCreateReq(
        @NotNull Long femObjId,
        @NotNull Long malObjId,
        @NotBlank String matDt,
        String note
) {}

