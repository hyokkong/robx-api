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

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileWebCtr {
    private final ProfileUseCase useCase;

    @GetMapping("/{userId}/public")
    public ApiResponse<ProfilePublic> getPublic(@PathVariable Long userId) {
        return ApiResponse.success(useCase.getPublicProfile(userId));
    }

    @GetMapping("/{userId}/summary")
    public ApiResponse<ProfileSummary> summary(@PathVariable Long userId) {
        return ApiResponse.success(useCase.getSummary(userId));
    }

    @PatchMapping("/me")
    public ApiResponse<Map<String, Boolean>> updateMe(@RequestBody UpdateProfileCommand command) {
        useCase.updateMyProfile(1L, command);
        return ApiResponse.success(Map.of("ok", true));
    }

    @GetMapping("/{userId}/reviews")
    public ApiResponse<ReviewPage> listReviews(@PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return ApiResponse.success(useCase.listReviews(userId, page, size));
    }

    @PostMapping("/{userId}/reviews")
    public ApiResponse<Map<String, Long>> createReview(@PathVariable Long userId,
            @RequestBody CreateReviewCommand command) {
        Long id = useCase.createReview(1L, userId, command);
        return ApiResponse.success(Map.of("id", id));
    }

    @PostMapping("/reviews/{id}/report")
    public ApiResponse<Map<String, Long>> report(@PathVariable Long id, @RequestBody ReportReviewCommand command) {
        Long rptId = useCase.reportReview(id, 1L, command);
        return ApiResponse.success(Map.of("id", rptId));
    }

    @DeleteMapping("/reviews/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        useCase.deleteReview(id, 1L);
        return ApiResponse.success(null);
    }
}
