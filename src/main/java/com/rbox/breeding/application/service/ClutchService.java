package com.rbox.breeding.application.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.rbox.breeding.adapter.out.persistence.repository.ClutchEntity;
import com.rbox.breeding.adapter.out.persistence.repository.ClutchRepository;
import com.rbox.breeding.adapter.out.persistence.repository.PairingRepository;
import com.rbox.breeding.adapter.out.persistence.repository.PairingEntity;
import com.rbox.breeding.application.port.in.ClutchUseCase;
import com.rbox.breeding.application.port.in.CreateClutchCommand;
import com.rbox.breeding.application.port.in.UpdateClutchCommand;
import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.object.adapter.out.persistence.repository.ObjectEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectRepository;

/**
 * 클러치 관련 비즈니스 로직.
 */
@Service
@RequiredArgsConstructor
public class ClutchService implements ClutchUseCase {
    private final ClutchRepository repository;
    private final PairingRepository pairingRepository;
    private final ObjectRepository objectRepository;

    @Override
    public Long createClutch(CreateClutchCommand command, Long uid) {
        PairingEntity pairing = pairingRepository.findById(command.matId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "pairing not found"));
        ObjectEntity fem = objectRepository.findById(command.femObjId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        if (!uid.equals(fem.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }
        if (!pairing.femObjId().equals(command.femObjId())) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "femObj mismatch");
        }
        int cltNo = command.cltNo() != null ? command.cltNo() : nextCltNo(command.matId());
        for (ClutchEntity e : repository.findByMatId(command.matId())) {
            if (e.cltNo().equals(cltNo)) {
                throw new ApiException(ErrorCode.INVALID_REQUEST, "duplicate clutch number");
            }
        }
        if (command.layYmd() != null) {
            parseDate(command.layYmd());
        }
        ClutchEntity entity = new ClutchEntity(null, command.matId(), command.femObjId(), cltNo, command.layYmd(),
                command.eggCount(), "PLAN", "N");
        return repository.save(entity);
    }

    @Override
    public List<ClutchEntity> listClutches(Long femObjId, String statCd, Long uid) {
        ObjectEntity fem = objectRepository.findById(femObjId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        if (!uid.equals(fem.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }
        List<ClutchEntity> list = repository.findByFemObjId(femObjId);
        if (statCd == null || statCd.isEmpty()) {
            return list;
        }
        List<ClutchEntity> filtered = new ArrayList<>();
        for (ClutchEntity e : list) {
            if (statCd.equals(e.statCd())) {
                filtered.add(e);
            }
        }
        return filtered;
    }

    @Override
    public void updateClutch(Long id, UpdateClutchCommand command, Long uid) {
        ClutchEntity c = repository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "clutch not found"));
        ObjectEntity fem = objectRepository.findById(c.femObjId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "object not found"));
        if (!uid.equals(fem.ownUsrId())) {
            throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
        }
        String statCd = command.statCd() != null ? command.statCd() : c.statCd();
        String chkYn = command.chkYn() != null ? command.chkYn() : c.chkYn();
        String layYmd = command.layYmd() != null ? command.layYmd() : c.layYmd();
        if (command.layYmd() != null) {
            parseDate(command.layYmd());
        }
        repository.update(new ClutchEntity(c.id(), c.matId(), c.femObjId(), c.cltNo(), layYmd, c.eggCount(), statCd, chkYn));
    }

    private int nextCltNo(Long matId) {
        return repository.findByMatId(matId).stream().map(ClutchEntity::cltNo).max(Integer::compare).orElse(0) + 1;
    }

    private void parseDate(String dt) {
        try {
            LocalDate.parse(dt);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "invalid date");
        }
    }
}

