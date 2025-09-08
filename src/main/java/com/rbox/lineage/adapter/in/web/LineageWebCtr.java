package com.rbox.lineage.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.lineage.application.port.in.LineageGraph;
import com.rbox.lineage.application.port.in.LineageUseCase;

/**
 * 계보 조회 API 컨트롤러.
 */
@RestController
@RequestMapping("/lineage")
@RequiredArgsConstructor
public class LineageWebCtr {
    private final LineageUseCase useCase;

    @GetMapping("/{id}/ancestors")
    public ApiResponse<LineageGraph> ancestors(@PathVariable Long id,
            @RequestParam(name = "depth", defaultValue = "3") int depth) {
        // 데모용으로 uid=1 고정
        return ApiResponse.success(useCase.getAncestors(1L, id, depth));
    }
}
