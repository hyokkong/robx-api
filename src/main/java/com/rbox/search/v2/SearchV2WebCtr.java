package com.rbox.search.v2;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.search.v2.SearchService.SearchResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller exposing the v2 search endpoints.
 */
@RestController
@RequestMapping("/v2/search")
@RequiredArgsConstructor
@Tag(name = "Search v2", description = "검색 API v2")
public class SearchV2WebCtr {
    private final SearchService service;

    @GetMapping("/listings")
    @Operation(summary = "리스트 검색", description = "리스트를 검색합니다")
    public ApiResponse<SearchResult> listings(
            @RequestParam(required = false, defaultValue = "ALL") String type,
            @RequestParam(required = false) String spcCd,
            @RequestParam(required = false) String sexCd,
            @RequestParam(required = false) String sizeCd,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false, defaultValue = "ANY") String morphMode,
            @RequestParam(required = false) String morphCat,
            @RequestParam(required = false) String morphIds,
            @RequestParam(required = false) Boolean hasImage,
            @RequestParam(required = false) Long ownerUserId,
            @RequestParam(required = false) Long ownerOrgId,
            @RequestParam(required = false, defaultValue = "-regDt") String sort,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        SearchListingsReq req = new SearchListingsReq(type, spcCd, sexCd, sizeCd,
                priceMin == null ? null : java.math.BigDecimal.valueOf(priceMin),
                priceMax == null ? null : java.math.BigDecimal.valueOf(priceMax),
                morphMode, morphCat, morphIds, hasImage, ownerUserId, ownerOrgId, sort, page, size);
        SearchResult result = service.searchListings(req);
        return ApiResponse.success(result);
    }

    @GetMapping("/objects")
    @Operation(summary = "개체 검색", description = "개체를 검색합니다")
    public ApiResponse<SearchResult> objects(
            @RequestParam(required = false) String spcCd,
            @RequestParam(required = false) String sexCd,
            @RequestParam(required = false) String sizeCd,
            @RequestParam(required = false, defaultValue = "ANY") String morphMode,
            @RequestParam(required = false) String morphIds,
            @RequestParam(required = false, defaultValue = "-regDt") String sort,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        SearchObjectsReq req = new SearchObjectsReq(spcCd, sexCd, sizeCd, morphMode, morphIds, sort, page, size);
        SearchResult result = service.searchObjects(req);
        return ApiResponse.success(result);
    }
}

