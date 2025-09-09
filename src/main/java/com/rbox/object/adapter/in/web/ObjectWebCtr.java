package com.rbox.object.adapter.in.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.object.adapter.out.persistence.repository.ObjectEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectImageEntity;
import com.rbox.common.qr.QrCodeService;
import com.rbox.object.application.port.in.AddImageCommand;
import com.rbox.object.application.port.in.CreateObjectCommand;
import com.rbox.object.ObjectServiceDelegate;
import com.rbox.object.application.port.in.UpdateObjectCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 개체(Object) 관련 API를 제공하는 Web Controller.
 */
@RestController
@RequestMapping("/objects")
@RequiredArgsConstructor
@Tag(name = "Object", description = "개체 관련 API")
public class ObjectWebCtr {
    private final ObjectServiceDelegate delegate;
    private final QrCodeService qrCodeService;

    /**
     * 심플 모드 개체 생성 API.
     * <p>필수 입력: spcCd, sexCd(M/F/U)</p>
     * <p>출력: 생성된 개체 ID</p>
     */
    @PostMapping
    @Operation(summary = "개체 생성", description = "새로운 개체를 생성합니다")
    public ApiResponse<Map<String, Long>> create(@Valid @RequestBody ObjectCreateReq req) {
        Long id = delegate.v1().createObject(new CreateObjectCommand(req.spcCd(), req.name(), req.sexCd(), req.bthYmd()), 1L);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        return ApiResponse.success(data);
    }

    /**
     * 내 개체 목록 조회 API.
     * <p>출력: 소유한 개체 리스트</p>
     */
    @GetMapping
    @Operation(summary = "내 개체 목록 조회", description = "소유한 개체 목록을 조회합니다")
    public ApiResponse<List<ObjectEntity>> list() {
        return ApiResponse.success(delegate.v1().listObjects(1L));
    }

    /**
     * 개체 상세 조회 API.
     * <p>필수 입력: 개체 ID</p>
     */
    @GetMapping("/{id}")
    @Operation(summary = "개체 상세 조회", description = "개체 ID로 상세 정보를 조회합니다")
    public ApiResponse<ObjectEntity> get(@PathVariable Long id) {
        return ApiResponse.success(delegate.v1().getObject(id, 1L));
    }

    /**
     * 개체 정보 수정 API.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "개체 정보 수정", description = "개체 정보를 수정합니다")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody ObjectUpdateReq req) {
        delegate.v1().updateObject(id, new UpdateObjectCommand(req.name(), req.sexCd(), req.bthYmd()), 1L);
        return ApiResponse.success(null);
    }

    /**
     * 개체 삭제 API.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "개체 삭제", description = "개체를 삭제합니다")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        delegate.v1().deleteObject(id, 1L);
        return ApiResponse.success(null);
    }

    /**
     * 개체 이미지 목록 조회 API.
     */
    @GetMapping("/{id}/images")
    @Operation(summary = "개체 이미지 목록 조회", description = "개체 이미지 목록을 조회합니다")
    public ApiResponse<List<ObjectImageEntity>> listImages(@PathVariable Long id) {
        return ApiResponse.success(delegate.v1().listImages(id, 1L));
    }

    /**
     * QR code for object.
     */
    @GetMapping(value = "/{id}/qrcode")
    @Operation(summary = "개체 QR 코드", description = "개체의 QR 코드를 생성합니다")
    public ResponseEntity<byte[]> qrcode(@PathVariable Long id,
            @RequestParam(required = false, defaultValue = "png") String fmt,
            @RequestParam(required = false, defaultValue = "256") Integer w) {
        // ensure object exists and belongs to user
        delegate.v1().getObject(id, 1L);
        String url = "https://app.rbox.io/o/" + id;
        byte[] data = qrCodeService.generate(url, w, fmt);
        MediaType media = "svg".equalsIgnoreCase(fmt) ? MediaType.valueOf("image/svg+xml") : MediaType.IMAGE_PNG;
        return ResponseEntity.ok().contentType(media).body(data);
    }

    /**
     * 개체 이미지 추가 API.
     */
    @PostMapping("/{id}/images")
    @Operation(summary = "개체 이미지 추가", description = "개체에 이미지를 추가합니다")
    public ApiResponse<Map<String, Long>> addImage(@PathVariable Long id, @Valid @RequestBody ObjectImageReq req) {
        Long imgId = delegate.v1().addImage(id, new AddImageCommand(req.url(), req.ordNo()), 1L);
        return ApiResponse.success(Map.of("imgId", imgId));
    }

    /**
     * 개체 이미지 삭제 API.
     */
    @DeleteMapping("/{id}/images/{imgId}")
    @Operation(summary = "개체 이미지 삭제", description = "개체 이미지를 삭제합니다")
    public ApiResponse<Void> deleteImage(@PathVariable Long id, @PathVariable Long imgId) {
        delegate.v1().deleteImage(id, imgId, 1L);
        return ApiResponse.success(null);
    }
}

