package com.rbox.admin.policy;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Policy", description = "관리자 정책 API")
public class AdminPolicyWebCtr {
    private final AdminPolicyService service;

    // Market policies
    @GetMapping("/market/policies")
    @Operation(summary = "마켓 정책 목록", description = "마켓 정책 목록을 조회합니다")
    public ApiResponse<Paged<MarketPolicy>> listMarket(@RequestParam(required = false) String useYn,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.listMarketPolicies(useYn, page, size));
    }

    @PutMapping("/market/policies/{polCd}")
    @Operation(summary = "마켓 정책 등록/수정", description = "마켓 정책을 등록하거나 수정합니다")
    public ApiResponse<MarketPolicy> upsertMarket(@RequestHeader("X-USER-ID") Long uid,
                                                   @PathVariable String polCd,
                                                   @Valid @RequestBody MarketPolicyReq req) {
        return ApiResponse.success(service.upsertMarketPolicy(polCd, req, uid));
    }

    @DeleteMapping("/market/policies/{polCd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "마켓 정책 삭제", description = "마켓 정책을 삭제합니다")
    public void deleteMarket(@RequestHeader("X-USER-ID") Long uid,
                             @PathVariable String polCd) {
        service.deleteMarketPolicy(polCd, uid);
    }

    // Breeding policies
    @GetMapping("/breeding/policies/{polCd}")
    @Operation(summary = "브리딩 정책 조회", description = "브리딩 정책을 조회합니다")
    public ApiResponse<BreedingPolicy> getBreeding(@PathVariable String polCd) {
        return ApiResponse.success(service.getBreedingPolicy(polCd));
    }

    @PutMapping("/breeding/policies/{polCd}")
    @Operation(summary = "브리딩 정책 등록/수정", description = "브리딩 정책을 등록하거나 수정합니다")
    public ApiResponse<BreedingPolicy> upsertBreeding(@RequestHeader("X-USER-ID") Long uid,
                                                       @PathVariable String polCd,
                                                       @Valid @RequestBody BreedingPolicyReq req) {
        return ApiResponse.success(service.upsertBreedingPolicy(polCd, req, uid));
    }

    // Quality policies
    @GetMapping("/quality/policies/{polCd}")
    @Operation(summary = "퀄리티 정책 조회", description = "퀄리티 정책을 조회합니다")
    public ApiResponse<QualityPolicy> getQuality(@PathVariable String polCd) {
        return ApiResponse.success(service.getQualityPolicy(polCd));
    }

    @PutMapping("/quality/policies/{polCd}")
    @Operation(summary = "퀄리티 정책 등록/수정", description = "퀄리티 정책을 등록하거나 수정합니다")
    public ApiResponse<QualityPolicy> upsertQuality(@RequestHeader("X-USER-ID") Long uid,
                                                     @PathVariable String polCd,
                                                     @Valid @RequestBody QualityPolicyReq req) {
        return ApiResponse.success(service.upsertQualityPolicy(polCd, req, uid));
    }

    // Size policies
    @GetMapping("/size/policies")
    @Operation(summary = "사이즈 정책 목록", description = "사이즈 정책 목록을 조회합니다")
    public ApiResponse<Iterable<SizePolicy>> listSize(@RequestParam String spcCd) {
        return ApiResponse.success(service.listSizePolicies(spcCd));
    }

    @PutMapping("/size/policies/{spcCd}/{szCd}")
    @Operation(summary = "사이즈 정책 등록/수정", description = "사이즈 정책을 등록하거나 수정합니다")
    public ApiResponse<SizePolicy> upsertSize(@RequestHeader("X-USER-ID") Long uid,
                                               @PathVariable String spcCd, @PathVariable String szCd,
                                               @Valid @RequestBody SizePolicyReq req) {
        return ApiResponse.success(service.upsertSizePolicy(spcCd, szCd, req, uid));
    }

    @DeleteMapping("/size/policies/{spcCd}/{szCd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "사이즈 정책 삭제", description = "사이즈 정책을 삭제합니다")
    public void deleteSize(@RequestHeader("X-USER-ID") Long uid,
                           @PathVariable String spcCd, @PathVariable String szCd) {
        service.deleteSizePolicy(spcCd, szCd, uid);
    }
}
