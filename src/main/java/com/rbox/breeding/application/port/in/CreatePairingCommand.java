package com.rbox.breeding.application.port.in;

/**
 * 메이팅 생성 명령.
 */
public record CreatePairingCommand(
        Long femObjId,
        Long malObjId,
        String matDt,
        String note
) {}

