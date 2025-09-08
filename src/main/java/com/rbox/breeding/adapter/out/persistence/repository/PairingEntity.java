package com.rbox.breeding.adapter.out.persistence.repository;

/**
 * 메이팅(Pairing) 정보를 보관하는 엔티티.
 */
public record PairingEntity(
        Long id,
        Long femObjId,
        Long malObjId,
        String matDt,
        String statCd,
        String note
) {}

