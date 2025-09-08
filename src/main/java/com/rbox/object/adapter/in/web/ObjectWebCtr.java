package com.rbox.object.adapter.in.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.object.adapter.out.persistence.repository.ObjectEntity;
import com.rbox.object.adapter.out.persistence.repository.ObjectImageEntity;
import com.rbox.object.application.port.in.AddImageCommand;
import com.rbox.object.application.port.in.CreateObjectCommand;
import com.rbox.object.application.port.in.ObjectUseCase;
import com.rbox.object.application.port.in.UpdateObjectCommand;

/**
 * 개체(Object) 관련 API를 제공하는 Web Controller.
 */
@RestController
@RequestMapping("/objects")
@RequiredArgsConstructor
public class ObjectWebCtr {
    private final ObjectUseCase useCase;

    /**
     * 심플 모드 개체 생성 API.
     * <p>필수 입력: spcCd, sexCd(M/F/U)</p>
     * <p>출력: 생성된 개체 ID</p>
     */
    @PostMapping
    public ApiResponse<Map<String, Long>> create(@Valid @RequestBody ObjectCreateReq req) {
        Long id = useCase.createObject(new CreateObjectCommand(req.spcCd(), req.name(), req.sexCd(), req.bthYmd()), 1L);
        Map<String, Long> data = new HashMap<>();
        data.put("id", id);
        return ApiResponse.success(data);
    }

    /**
     * 내 개체 목록 조회 API.
     * <p>출력: 소유한 개체 리스트</p>
     */
    @GetMapping
    public ApiResponse<List<ObjectEntity>> list() {
        return ApiResponse.success(useCase.listObjects(1L));
    }

    /**
     * 개체 상세 조회 API.
     * <p>필수 입력: 개체 ID</p>
     */
    @GetMapping("/{id}")
    public ApiResponse<ObjectEntity> get(@PathVariable Long id) {
        return ApiResponse.success(useCase.getObject(id, 1L));
    }

    /**
     * 개체 정보 수정 API.
     */
    @PatchMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody ObjectUpdateReq req) {
        useCase.updateObject(id, new UpdateObjectCommand(req.name(), req.sexCd(), req.bthYmd()), 1L);
        return ApiResponse.success(null);
    }

    /**
     * 개체 삭제 API.
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        useCase.deleteObject(id, 1L);
        return ApiResponse.success(null);
    }

    /**
     * 개체 이미지 목록 조회 API.
     */
    @GetMapping("/{id}/images")
    public ApiResponse<List<ObjectImageEntity>> listImages(@PathVariable Long id) {
        return ApiResponse.success(useCase.listImages(id, 1L));
    }

    /**
     * 개체 이미지 추가 API.
     */
    @PostMapping("/{id}/images")
    public ApiResponse<Map<String, Long>> addImage(@PathVariable Long id, @Valid @RequestBody ObjectImageReq req) {
        Long imgId = useCase.addImage(id, new AddImageCommand(req.url(), req.ordNo()), 1L);
        return ApiResponse.success(Map.of("imgId", imgId));
    }

    /**
     * 개체 이미지 삭제 API.
     */
    @DeleteMapping("/{id}/images/{imgId}")
    public ApiResponse<Void> deleteImage(@PathVariable Long id, @PathVariable Long imgId) {
        useCase.deleteImage(id, imgId, 1L);
        return ApiResponse.success(null);
    }
}

