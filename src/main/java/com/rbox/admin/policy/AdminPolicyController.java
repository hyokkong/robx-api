package com.rbox.admin.policy;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPolicyController {
    private final AdminPolicyService service;

    // Market policies
    @GetMapping("/market/policies")
    public ApiResponse<Paged<MarketPolicy>> listMarket(@RequestParam(required = false) String useYn,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.listMarketPolicies(useYn, page, size));
    }

    @PutMapping("/market/policies/{polCd}")
    public ApiResponse<MarketPolicy> upsertMarket(@RequestHeader("X-USER-ID") Long uid,
                                                   @PathVariable String polCd,
                                                   @Valid @RequestBody MarketPolicyRequest req) {
        return ApiResponse.success(service.upsertMarketPolicy(polCd, req, uid));
    }

    @DeleteMapping("/market/policies/{polCd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarket(@RequestHeader("X-USER-ID") Long uid,
                             @PathVariable String polCd) {
        service.deleteMarketPolicy(polCd, uid);
    }

    // Breeding policies
    @GetMapping("/breeding/policies/{polCd}")
    public ApiResponse<BreedingPolicy> getBreeding(@PathVariable String polCd) {
        return ApiResponse.success(service.getBreedingPolicy(polCd));
    }

    @PutMapping("/breeding/policies/{polCd}")
    public ApiResponse<BreedingPolicy> upsertBreeding(@RequestHeader("X-USER-ID") Long uid,
                                                       @PathVariable String polCd,
                                                       @Valid @RequestBody BreedingPolicyRequest req) {
        return ApiResponse.success(service.upsertBreedingPolicy(polCd, req, uid));
    }

    // Quality policies
    @GetMapping("/quality/policies/{polCd}")
    public ApiResponse<QualityPolicy> getQuality(@PathVariable String polCd) {
        return ApiResponse.success(service.getQualityPolicy(polCd));
    }

    @PutMapping("/quality/policies/{polCd}")
    public ApiResponse<QualityPolicy> upsertQuality(@RequestHeader("X-USER-ID") Long uid,
                                                     @PathVariable String polCd,
                                                     @Valid @RequestBody QualityPolicyRequest req) {
        return ApiResponse.success(service.upsertQualityPolicy(polCd, req, uid));
    }

    // Size policies
    @GetMapping("/size/policies")
    public ApiResponse<Iterable<SizePolicy>> listSize(@RequestParam String spcCd) {
        return ApiResponse.success(service.listSizePolicies(spcCd));
    }

    @PutMapping("/size/policies/{spcCd}/{szCd}")
    public ApiResponse<SizePolicy> upsertSize(@RequestHeader("X-USER-ID") Long uid,
                                               @PathVariable String spcCd, @PathVariable String szCd,
                                               @Valid @RequestBody SizePolicyRequest req) {
        return ApiResponse.success(service.upsertSizePolicy(spcCd, szCd, req, uid));
    }

    @DeleteMapping("/size/policies/{spcCd}/{szCd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSize(@RequestHeader("X-USER-ID") Long uid,
                           @PathVariable String spcCd, @PathVariable String szCd) {
        service.deleteSizePolicy(spcCd, szCd, uid);
    }
}
