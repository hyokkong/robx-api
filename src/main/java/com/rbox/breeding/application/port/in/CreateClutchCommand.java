package com.rbox.breeding.application.port.in;

/**
 * 클러치 생성 명령.
 */
public record CreateClutchCommand(
        Long matId,
        Long femObjId,
        Integer cltNo,
        String layYmd,
        Integer eggCount
) {}

