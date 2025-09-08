package com.rbox.snapshot.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.lineage.application.port.in.LineageGraph;
import com.rbox.snapshot.application.port.in.SnapshotCreateCommand;
import com.rbox.snapshot.application.port.in.SnapshotCreateResp;
import com.rbox.snapshot.application.port.in.SnapshotUseCase;
import com.rbox.common.qr.QrCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 3세대 계보 스냅샷 API.
 */
@RestController
@RequestMapping("/snapshots/3gen")
@RequiredArgsConstructor
@Tag(name = "Snapshot", description = "3세대 계보 스냅샷 API")
public class SnapshotWebCtr {
    private final SnapshotUseCase useCase;
    private final QrCodeService qrCodeService;

    @PostMapping
    @Operation(summary = "스냅샷 생성", description = "3세대 계보 스냅샷을 생성합니다")
    public ResponseEntity<ApiResponse<SnapshotCreateResp>> create(@Valid @RequestBody SnapshotCreateReq req) {
        SnapshotCreateResp resp = useCase.createSnapshot(
                new SnapshotCreateCommand(req.rootObjId(), req.ttlDays(), req.note()), 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(resp));
    }

    @GetMapping("/{token}")
    @Operation(summary = "스냅샷 조회", description = "토큰으로 스냅샷을 조회합니다")
    public ApiResponse<LineageGraph> get(@PathVariable String token) {
        return ApiResponse.success(useCase.getSnapshot(token));
    }

    /**
     * QR code for snapshot token.
     */
    @GetMapping("/{token}/qrcode")
    @Operation(summary = "스냅샷 QR 코드", description = "스냅샷 토큰의 QR 코드를 생성합니다")
    public ResponseEntity<byte[]> qrcode(@PathVariable String token,
            @RequestParam(required = false, defaultValue = "png") String fmt,
            @RequestParam(required = false, defaultValue = "256") Integer w) {
        useCase.getSnapshot(token);
        String url = "https://app.rbox.io/s/" + token;
        byte[] data = qrCodeService.generate(url, w, fmt);
        MediaType media = "svg".equalsIgnoreCase(fmt) ? MediaType.valueOf("image/svg+xml") : MediaType.IMAGE_PNG;
        return ResponseEntity.ok().contentType(media).body(data);
    }
}
