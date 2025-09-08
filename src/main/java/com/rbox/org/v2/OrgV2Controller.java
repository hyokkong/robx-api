package com.rbox.org.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for organization management APIs (v2).
 */
@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
@Tag(name = "Organization v2", description = "조직 관리 API v2")
public class OrgV2Controller {
    private final OrgService service;

    private Long getUserId(String header) {
        return header == null ? null : Long.valueOf(header);
    }

    @PostMapping("/orgs")
    @Operation(summary = "조직 생성", description = "새로운 조직을 생성합니다")
    public ApiResponse<Map<String, Long>> createOrg(@RequestHeader("X-USER-ID") String user,
            @RequestBody Map<String, String> body) {
        String orgNm = body.getOrDefault("orgNm", "");
        String orgTp = body.getOrDefault("orgTp", "SHOP");
        OrgService.Org org = service.createOrg(getUserId(user), orgNm, orgTp);
        return ApiResponse.success(Map.of("orgId", org.orgId()));
    }

    @GetMapping("/orgs")
    @Operation(summary = "조직 목록 조회", description = "사용자의 조직 목록을 조회합니다")
    public ApiResponse<List<OrgService.Org>> listOrgs(@RequestHeader("X-USER-ID") String user) {
        List<OrgService.Org> list = service.listOrgs(getUserId(user));
        return ApiResponse.success(list);
    }

    @PostMapping("/orgs/{orgId}/members")
    @Operation(summary = "멤버 추가", description = "조직에 멤버를 추가합니다")
    public ApiResponse<Void> addMember(@PathVariable Long orgId, @RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        String roleCd = (String) body.get("roleCd");
        service.addMember(orgId, userId, roleCd);
        return ApiResponse.success(null);
    }

    @PatchMapping("/orgs/{orgId}/members/{userId}")
    @Operation(summary = "멤버 역할 변경", description = "조직 멤버의 역할을 변경합니다")
    public ApiResponse<Void> changeMember(@PathVariable Long orgId, @PathVariable Long userId,
            @RequestBody Map<String, String> body) {
        String roleCd = body.get("roleCd");
        service.changeMember(orgId, userId, roleCd);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/orgs/{orgId}/members/{userId}")
    @Operation(summary = "멤버 삭제", description = "조직에서 멤버를 삭제합니다")
    public ApiResponse<Void> deleteMember(@PathVariable Long orgId, @PathVariable Long userId) {
        service.deleteMember(orgId, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/context")
    @Operation(summary = "조직 컨텍스트 조회", description = "요청 헤더의 조직/사용자 정보를 반환합니다")
    public ApiResponse<Map<String, Object>> context(
            @RequestHeader(value = "X-USER-ID", required = false) String user,
            @RequestHeader(value = "X-RBOX-ORG-ID", required = false) String org) {
        Map<String, Object> data = new HashMap<>();
        if (user != null) data.put("userId", Long.valueOf(user));
        if (org != null) data.put("orgId", Long.valueOf(org));
        return ApiResponse.success(data);
    }
}

