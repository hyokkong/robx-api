package com.rbox.object.adapter.out.persistence.repository;

/**
 * 개체(Object) 정보를 보관하는 엔티티.
 */
public record ObjectEntity(
        Long id,
        String spcCd,
        String name,
        String sexCd,
        String bthYmd,
        String objMode,
        String statCd,
        String marketOk,
        Long ownUsrId
) {}

