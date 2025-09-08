package com.rbox.breeding.adapter.out.persistence.repository;

/**
 * 클러치(산란 기록) 정보를 보관하는 엔티티.
 */
public record ClutchEntity(
        Long id,
        Long matId,
        Long femObjId,
        Integer cltNo,
        String layYmd,
        Integer eggCount,
        String statCd,
        String chkYn
) {}

