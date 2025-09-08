package com.rbox.dashboard.adapter.in.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbox.common.api.ApiResponse;

/**
 * 사용자 대시보드 요약 API.
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardWebCtr {

    /**
     * 오늘/이번주 처리해야 할 브리딩 작업 요약을 반환한다.
     */
    @GetMapping("/overview")
    public ApiResponse<Map<String, Object>> overview() {
        // TODO 실제 데이터 연동 필요
        Map<String, Integer> today = Map.of(
                "layEtd", 1,
                "layChk", 2,
                "hatEtd", 1
        );

        Map<String, Integer> week = Map.of(
                "layEtd", 2,
                "layChk", 3,
                "hatEtd", 2
        );

        List<Map<String, Object>> highlights = List.of(
                Map.of(
                        "type", "LAY_CHK",
                        "title", "산란 확인 필요",
                        "femObjId", 101L,
                        "cltId", 7001L,
                        "due", LocalDate.now().toString()
                )
        );

        Map<String, Object> data = Map.of(
                "today", today,
                "week", week,
                "highlights", highlights
        );

        return ApiResponse.success(data);
    }
}

