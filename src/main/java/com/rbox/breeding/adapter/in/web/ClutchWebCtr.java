package com.rbox.breeding.adapter.in.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.breeding.adapter.out.persistence.repository.ClutchEntity;
import com.rbox.breeding.application.port.in.ClutchUseCase;
import com.rbox.breeding.application.port.in.CreateClutchCommand;
import com.rbox.breeding.application.port.in.UpdateClutchCommand;
import com.rbox.common.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 클러치 관련 API.
 */
@RestController
@RequestMapping("/clutches")
@RequiredArgsConstructor
@Tag(name = "Clutch", description = "클러치 관련 API")
public class ClutchWebCtr {
    private final ClutchUseCase useCase;

    @PostMapping
    @Operation(summary = "클러치 생성", description = "새로운 클러치를 생성합니다")
    public ResponseEntity<ApiResponse<Map<String, Long>>> create(@Valid @RequestBody ClutchCreateReq req) {
        Long id = useCase.createClutch(new CreateClutchCommand(req.matId(), req.femObjId(), req.cltNo(), req.layYmd(),
                req.eggCount()), 1L);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data));
    }

    @GetMapping
    @Operation(summary = "클러치 목록 조회", description = "클러치 목록을 조회합니다")
    public ApiResponse<List<ClutchEntity>> list(@RequestParam Long femObjId,
            @RequestParam(required = false) String statCd) {
        return ApiResponse.success(useCase.listClutches(femObjId, statCd, 1L));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "클러치 수정", description = "클러치 정보를 수정합니다")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody ClutchPatchReq req) {
        useCase.updateClutch(id, new UpdateClutchCommand(req.statCd(), req.chkYn(), req.layYmd()), 1L);
        return ApiResponse.success(null);
    }
}

