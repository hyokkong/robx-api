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

/**
 * Controller for organization management APIs (v2).
 */
@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
public class OrgV2Controller {
    private final OrgService service;

    private Long getUserId(String header) {
        return header == null ? null : Long.valueOf(header);
    }

    @PostMapping("/orgs")
    public ApiResponse<Map<String, Long>> createOrg(@RequestHeader("X-USER-ID") String user,
            @RequestBody Map<String, String> body) {
        String orgNm = body.getOrDefault("orgNm", "");
        String orgTp = body.getOrDefault("orgTp", "SHOP");
        OrgService.Org org = service.createOrg(getUserId(user), orgNm, orgTp);
        return ApiResponse.success(Map.of("orgId", org.orgId()));
    }

    @GetMapping("/orgs")
    public ApiResponse<List<OrgService.Org>> listOrgs(@RequestHeader("X-USER-ID") String user) {
        List<OrgService.Org> list = service.listOrgs(getUserId(user));
        return ApiResponse.success(list);
    }

    @PostMapping("/orgs/{orgId}/members")
    public ApiResponse<Void> addMember(@PathVariable Long orgId, @RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        String roleCd = (String) body.get("roleCd");
        service.addMember(orgId, userId, roleCd);
        return ApiResponse.success(null);
    }

    @PatchMapping("/orgs/{orgId}/members/{userId}")
    public ApiResponse<Void> changeMember(@PathVariable Long orgId, @PathVariable Long userId,
            @RequestBody Map<String, String> body) {
        String roleCd = body.get("roleCd");
        service.changeMember(orgId, userId, roleCd);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/orgs/{orgId}/members/{userId}")
    public ApiResponse<Void> deleteMember(@PathVariable Long orgId, @PathVariable Long userId) {
        service.deleteMember(orgId, userId);
        return ApiResponse.success(null);
    }

    @GetMapping("/context")
    public ApiResponse<Map<String, Object>> context(
            @RequestHeader(value = "X-USER-ID", required = false) String user,
            @RequestHeader(value = "X-RBOX-ORG-ID", required = false) String org) {
        Map<String, Object> data = new HashMap<>();
        if (user != null) data.put("userId", Long.valueOf(user));
        if (org != null) data.put("orgId", Long.valueOf(org));
        return ApiResponse.success(data);
    }
}

