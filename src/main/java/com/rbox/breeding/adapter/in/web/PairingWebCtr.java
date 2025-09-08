package com.rbox.breeding.adapter.in.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.breeding.adapter.out.persistence.repository.PairingEntity;
import com.rbox.breeding.application.port.in.CreatePairingCommand;
import com.rbox.breeding.application.port.in.PairingUseCase;
import com.rbox.common.api.ApiResponse;

/**
 * 메이팅 관련 API.
 */
@RestController
@RequestMapping("/pairings")
@RequiredArgsConstructor
public class PairingWebCtr {
    private final PairingUseCase useCase;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(@Valid @RequestBody PairingCreateReq req) {
        Long id = useCase.createPairing(new CreatePairingCommand(req.femObjId(), req.malObjId(), req.matDt(), req.note()), 1L);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data));
    }

    @GetMapping
    public ApiResponse<List<PairingEntity>> list(@RequestParam Long femObjId) {
        return ApiResponse.success(useCase.listPairings(femObjId, 1L));
    }
}

