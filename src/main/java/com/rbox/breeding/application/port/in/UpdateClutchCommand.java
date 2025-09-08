package com.rbox.breeding.application.port.in;

/**
 * 클러치 수정 명령.
 */
public record UpdateClutchCommand(
        String statCd,
        String chkYn,
        String layYmd
) {}

