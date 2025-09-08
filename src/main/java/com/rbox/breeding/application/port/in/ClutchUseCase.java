package com.rbox.breeding.application.port.in;

import java.util.List;

import com.rbox.breeding.adapter.out.persistence.repository.ClutchEntity;

/**
 * 클러치 관련 UseCase.
 */
public interface ClutchUseCase {
    Long createClutch(CreateClutchCommand command, Long uid);
    List<ClutchEntity> listClutches(Long femObjId, String statCd, Long uid);
    void updateClutch(Long id, UpdateClutchCommand command, Long uid);
}

