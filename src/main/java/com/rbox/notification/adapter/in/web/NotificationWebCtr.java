package com.rbox.notification.adapter.in.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbox.common.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 브리딩 관련 알림 요약 API.
 */
@RestController
@RequestMapping("/notifications")
@Tag(name = "Notification", description = "브리딩 관련 알림 API")
public class NotificationWebCtr {

    /**
     * 로그인 사용자의 다가오는 알림 목록을 조회한다.
     *
     * @param type 알림 종류 필터 (옵션)
     * @return 알림 목록 및 페이지 정보
     */
    @GetMapping("/upcoming")
    @Operation(summary = "다가오는 알림 목록", description = "로그인 사용자의 다가오는 알림을 조회합니다")
    public ApiResponse<List<Map<String, Object>>> upcoming(
            @RequestParam(name = "type", required = false) String type) {
        // TODO 실제 구현에서는 서비스/리포지토리 연동 필요
        List<Map<String, Object>> data = List.of(
                Map.of(
                        "type", "LAY_ETD",
                        "dueDt", LocalDateTime.now().plusDays(1).toString(),
                        "ref", Map.of("pairingId", 5001L, "femObjId", 101L, "malObjId", 202L),
                        "message", "암컷 #101 산란 예정"
                ),
                Map.of(
                        "type", "HAT_ETD",
                        "dueDt", LocalDateTime.now().plusDays(5).toString(),
                        "ref", Map.of("eggId", 73005L, "cltId", 7001L, "femObjId", 101L),
                        "message", "클러치 #7001 알 #5 해칭 예정"
                )
        );

        Map<String, Object> meta = Map.of(
                "page", 1,
                "size", data.size(),
                "total", data.size()
        );

        return new ApiResponse<>(data, meta, null);
    }
}

