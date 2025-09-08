package com.rbox.breeding.application.port.in;

import java.util.List;

import com.rbox.breeding.adapter.out.persistence.repository.PairingEntity;

/**
 * 메이팅 관련 UseCase.
 */
public interface PairingUseCase {
    Long createPairing(CreatePairingCommand command, Long uid);
    List<PairingEntity> listPairings(Long femObjId, Long uid);
}

