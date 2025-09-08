package com.rbox.breeding.adapter.in.web;

/**
 * 클러치 수정 요청.
 */
public record ClutchPatchReq(
        String statCd,
        String chkYn,
        String layYmd
) {}

