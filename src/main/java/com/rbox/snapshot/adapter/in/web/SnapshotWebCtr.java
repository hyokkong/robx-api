package com.rbox.snapshot.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.lineage.application.port.in.LineageGraph;
import com.rbox.snapshot.application.port.in.SnapshotCreateCommand;
import com.rbox.snapshot.application.port.in.SnapshotCreateResp;
import com.rbox.snapshot.application.port.in.SnapshotUseCase;

/**
 * 3세대 계보 스냅샷 API.
 */
@RestController
@RequestMapping("/snapshots/3gen")
@RequiredArgsConstructor
public class SnapshotWebCtr {
    private final SnapshotUseCase useCase;

    @PostMapping
    public ResponseEntity<ApiResponse<SnapshotCreateResp>> create(@Valid @RequestBody SnapshotCreateReq req) {
        SnapshotCreateResp resp = useCase.createSnapshot(
                new SnapshotCreateCommand(req.rootObjId(), req.ttlDays(), req.note()), 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(resp));
    }

    @GetMapping("/{token}")
    public ApiResponse<LineageGraph> get(@PathVariable String token) {
        return ApiResponse.success(useCase.getSnapshot(token));
    }
}
