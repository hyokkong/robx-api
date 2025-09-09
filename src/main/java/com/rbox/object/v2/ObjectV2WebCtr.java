package com.rbox.object.v2;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.object.ObjectServiceDelegate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller exposing the v2 object endpoints.
 */
@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
@Tag(name = "Object v2", description = "개체 API v2")
public class ObjectV2WebCtr {
    private final ObjectServiceDelegate delegate;

    @PostMapping("/objects")
    @Operation(summary = "개체 생성", description = "새로운 개체를 생성합니다")
    public ApiResponse<Map<String, Long>> create(
            @RequestHeader(value = "X-RBOX-ORG-ID", required = false) Long orgId,
            @RequestBody ObjectCreateReq req) {
        Long id = delegate.v2().createObject(req.spcCd(), req.name(), req.sexCd(), req.ownerType(), orgId);
        return ApiResponse.success(Map.of("objId", id));
    }
}
