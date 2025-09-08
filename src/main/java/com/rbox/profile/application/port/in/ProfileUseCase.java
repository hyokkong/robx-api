package com.rbox.profile.application.port.in;

public interface ProfileUseCase {
    ProfilePublic getPublicProfile(Long userId);
    ProfileSummary getSummary(Long userId);
    void updateMyProfile(Long uid, UpdateProfileCommand command);
    ReviewPage listReviews(Long userId, int page, int size);
    Long createReview(Long uid, Long targetUserId, CreateReviewCommand command);
    Long reportReview(Long reviewId, Long uid, ReportReviewCommand command);
    void deleteReview(Long reviewId, Long uid);
}
