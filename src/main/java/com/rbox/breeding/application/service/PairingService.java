package com.rbox.breeding.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.rbox.breeding.adapter.out.persistence.repository.PairingEntity;
import com.rbox.breeding.adapter.out.persistence.repository.PairingRepository;
import com.rbox.breeding.application.port.in.CreatePairingCommand;
import com.rbox.breeding.application.port.in.PairingUseCase;
import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.object.adapter.out.persistence.repository.ObjectEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectRepository;

/**
 * 메이팅 관련 비즈니스 로직.
 */
@Service
@RequiredArgsConstructor
public class PairingService implements PairingUseCase {
    private final PairingRepository repository;
    private final ObjectRepository objectRepository;

    @Override
    public Long createPairing(CreatePairingCommand command, Long uid) {
        ObjectEntity fem = objectRepository.findById(command.femObjId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        ObjectEntity mal = objectRepository.findById(command.malObjId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        if (!uid.equals(fem.ownUsrId()) || !uid.equals(mal.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }
        if (!"ACTV".equals(fem.statCd()) || !"ACTV".equals(mal.statCd())) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "inactive object");
        }
        if (!"F".equals(fem.sexCd()) || !"M".equals(mal.sexCd())) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "invalid sex");
        }
        parseDate(command.matDt());
        return repository.save(new PairingEntity(null, command.femObjId(), command.malObjId(), command.matDt(), "ACTV",
                command.note()));
    }

    @Override
    public List<PairingEntity> listPairings(Long femObjId, Long uid) {
        ObjectEntity fem = objectRepository.findById(femObjId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        if (!uid.equals(fem.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }
        return repository.findByFemObjId(femObjId);
    }

    private void parseDate(String dt) {
        try {
            LocalDate.parse(dt);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "invalid date");
        }
    }
}

