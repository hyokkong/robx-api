package com.rbox.search.v2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;

/**
 * Controller exposing the v2 search endpoints.
 */
@RestController
@RequestMapping("/v2/search")
@RequiredArgsConstructor
public class SearchV2Controller {
    private final SearchService service;

    @GetMapping("/listings")
    public ApiResponse<Map<String, Object>> listings(
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
        SearchListingsRequest req = new SearchListingsRequest(type, spcCd, sexCd, sizeCd,
                priceMin == null ? null : java.math.BigDecimal.valueOf(priceMin),
                priceMax == null ? null : java.math.BigDecimal.valueOf(priceMax),
                morphMode, morphCat, morphIds, hasImage, ownerUserId, ownerOrgId, sort, page, size);
        SearchService.SearchResult result = service.searchListings(req);
        Map<String, Object> data = new HashMap<>();
        data.put("content", result.content());
        data.put("total", result.total());
        data.put("page", result.page());
        data.put("size", result.size());
        return ApiResponse.success(data);
    }

    @GetMapping("/objects")
    public ApiResponse<Map<String, Object>> objects(
            @RequestParam(required = false) String spcCd,
            @RequestParam(required = false) String sexCd,
            @RequestParam(required = false) String sizeCd,
            @RequestParam(required = false, defaultValue = "ANY") String morphMode,
            @RequestParam(required = false) String morphIds,
            @RequestParam(required = false, defaultValue = "-regDt") String sort,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        SearchObjectsRequest req = new SearchObjectsRequest(spcCd, sexCd, sizeCd, morphMode, morphIds, sort, page, size);
        SearchService.SearchResult result = service.searchObjects(req);
        Map<String, Object> data = new HashMap<>();
        data.put("content", result.content());
        data.put("total", result.total());
        data.put("page", result.page());
        data.put("size", result.size());
        return ApiResponse.success(data);
    }
}

