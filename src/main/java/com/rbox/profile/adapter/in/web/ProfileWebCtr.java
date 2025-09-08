package com.rbox.profile.adapter.in.web;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.rbox.common.api.ApiResponse;
import com.rbox.profile.application.port.in.CreateReviewCommand;
import com.rbox.profile.application.port.in.ProfilePublic;
import com.rbox.profile.application.port.in.ProfileSummary;
import com.rbox.profile.application.port.in.ProfileUseCase;
import com.rbox.profile.application.port.in.ReviewPage;
import com.rbox.profile.application.port.in.ReportReviewCommand;
import com.rbox.profile.application.port.in.UpdateProfileCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "프로필 관련 API")
public class ProfileWebCtr {
    private final ProfileUseCase useCase;

    @GetMapping("/{userId}/public")
    @Operation(summary = "공개 프로필 조회", description = "사용자의 공개 프로필을 조회합니다")
    public ApiResponse<ProfilePublic> getPublic(@PathVariable Long userId) {
        return ApiResponse.success(useCase.getPublicProfile(userId));
    }

    @GetMapping("/{userId}/summary")
    @Operation(summary = "프로필 요약", description = "사용자의 프로필 요약 정보를 조회합니다")
    public ApiResponse<ProfileSummary> summary(@PathVariable Long userId) {
        return ApiResponse.success(useCase.getSummary(userId));
    }

    @PatchMapping("/me")
    @Operation(summary = "내 프로필 수정", description = "내 프로필 정보를 수정합니다")
    public ApiResponse<Map<String, Boolean>> updateMe(@RequestBody UpdateProfileCommand command) {
        useCase.updateMyProfile(1L, command);
        return ApiResponse.success(Map.of("ok", true));
    }

    @GetMapping("/{userId}/reviews")
    @Operation(summary = "리뷰 목록 조회", description = "사용자 리뷰 목록을 조회합니다")
    public ApiResponse<ReviewPage> listReviews(@PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return ApiResponse.success(useCase.listReviews(userId, page, size));
    }

    @PostMapping("/{userId}/reviews")
    @Operation(summary = "리뷰 작성", description = "사용자에 대한 리뷰를 작성합니다")
    public ApiResponse<Map<String, Long>> createReview(@PathVariable Long userId,
            @RequestBody CreateReviewCommand command) {
        Long id = useCase.createReview(1L, userId, command);
        return ApiResponse.success(Map.of("id", id));
    }

    @PostMapping("/reviews/{id}/report")
    @Operation(summary = "리뷰 신고", description = "리뷰를 신고합니다")
    public ApiResponse<Map<String, Long>> report(@PathVariable Long id, @RequestBody ReportReviewCommand command) {
        Long rptId = useCase.reportReview(id, 1L, command);
        return ApiResponse.success(Map.of("id", rptId));
    }

    @DeleteMapping("/reviews/{id}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        useCase.deleteReview(id, 1L);
        return ApiResponse.success(null);
    }
}
