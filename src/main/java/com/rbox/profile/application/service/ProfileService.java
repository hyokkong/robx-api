package com.rbox.profile.application.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rbox.common.api.ErrorCode;
import com.rbox.common.exception.ApiException;
import com.rbox.profile.application.port.in.CreateReviewCommand;
import com.rbox.profile.application.port.in.ProfilePublic;
import com.rbox.profile.application.port.in.ProfileSummary;
import com.rbox.profile.application.port.in.ProfileUseCase;
import com.rbox.profile.application.port.in.ReviewItem;
import com.rbox.profile.application.port.in.ReviewPage;
import com.rbox.profile.application.port.in.ReviewRelation;
import com.rbox.profile.application.port.in.ReviewSummary;
import com.rbox.profile.application.port.in.ReviewUser;
import com.rbox.profile.application.port.in.ReportReviewCommand;
import com.rbox.profile.application.port.in.UpdateProfileCommand;

@Service
public class ProfileService implements ProfileUseCase {
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Map<Long, List<Review>> reviews = new HashMap<>();
    private long reviewSeq = 9000;
    private long reportSeq = 8800;

    public ProfileService() {
        profiles.put(501L, new Profile(501L, "ReptiKing", "L2",
                new HashMap<>(Map.of("seller", "silver", "pro", "L2")),
                "10년차 브리더", "https://insta.com/reptiking", 4.7, 83));
    }

    @Override
    public ProfilePublic getPublicProfile(Long userId) {
        Profile p = profiles.get(userId);
        if (p == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "profile not found");
        }
        return new ProfilePublic(p.userId(), p.nick(), p.profLv(), p.badges(), p.bio(), p.linkUrl(),
                new ReviewSummary(p.revAvg(), p.revCnt()));
    }

    @Override
    public ProfileSummary getSummary(Long userId) {
        Profile p = profiles.get(userId);
        if (p == null) {
            throw new ApiException(ErrorCode.NOT_FOUND, "profile not found");
        }
        return new ProfileSummary(p.profLv(), p.badges(), new ReviewSummary(p.revAvg(), p.revCnt()));
    }

    @Override
    public void updateMyProfile(Long uid, UpdateProfileCommand command) {
        Profile p = profiles.get(uid);
        if (p == null) {
            p = new Profile(uid, "user" + uid, "L0", new HashMap<>(), null, null, 0.0, 0);
            profiles.put(uid, p);
        }
        String bio = command.bio() != null ? command.bio() : p.bio();
        String linkUrl = command.linkUrl() != null ? command.linkUrl() : p.linkUrl();
        Map<String, String> badges = command.badges() != null ? new HashMap<>(command.badges()) : p.badges();
        profiles.put(uid, new Profile(uid, p.nick(), p.profLv(), badges, bio, linkUrl, p.revAvg(), p.revCnt()));
    }

    @Override
    public ReviewPage listReviews(Long userId, int page, int size) {
        List<Review> list = reviews.getOrDefault(userId, List.of());
        List<ReviewItem> content = new ArrayList<>();
        for (Review r : list) {
            if (!"PUB".equals(r.statCd())) {
                continue;
            }
            content.add(toItem(r));
        }
        long total = content.size();
        int from = Math.min((page - 1) * size, content.size());
        int to = Math.min(from + size, content.size());
        List<ReviewItem> pageList = content.subList(from, to);
        return new ReviewPage(pageList, total, page, size);
    }

    @Override
    public Long createReview(Long uid, Long targetUserId, CreateReviewCommand command) {
        if (command.rating() < 1 || command.rating() > 5) {
            throw new ApiException(ErrorCode.INVALID_REQUEST, "rating must be 1..5");
        }
        reviewSeq++;
        Review r = new Review(reviewSeq, targetUserId, uid, "user" + uid, command.rating(), command.content(),
                ZonedDateTime.now().toString(),
                new ReviewRelation(command.auctionId() != null ? "AUCTION" : "OBJECT", command.auctionId(),
                        command.listingId(), command.objId()),
                "PUB");
        reviews.computeIfAbsent(targetUserId, k -> new ArrayList<>()).add(r);
        updateStats(targetUserId);
        return r.revId();
    }

    @Override
    public Long reportReview(Long reviewId, Long uid, ReportReviewCommand command) {
        reportSeq++;
        return reportSeq;
    }

    @Override
    public void deleteReview(Long reviewId, Long uid) {
        for (List<Review> list : reviews.values()) {
            for (Review r : list) {
                if (r.revId().equals(reviewId)) {
                    if (!r.frmUserId().equals(uid)) {
                        throw new ApiException(ErrorCode.FORBIDDEN, "forbidden");
                    }
                    r.statCd = "DEL";
                    updateStats(r.tgtUserId());
                    return;
                }
            }
        }
        throw new ApiException(ErrorCode.NOT_FOUND, "review not found");
    }

    private ReviewItem toItem(Review r) {
        return new ReviewItem(r.revId(), new ReviewUser(r.frmUserId(), r.frmNick()), r.rating(), r.content(),
                r.date(), r.rel());
    }

    private void updateStats(Long userId) {
        List<Review> list = reviews.getOrDefault(userId, List.of());
        int cnt = 0;
        int sum = 0;
        for (Review r : list) {
            if ("PUB".equals(r.statCd())) {
                cnt++;
                sum += r.rating();
            }
        }
        double avg = cnt > 0 ? ((double) sum) / cnt : 0.0;
        Profile p = profiles.get(userId);
        if (p != null) {
            profiles.put(userId, new Profile(p.userId(), p.nick(), p.profLv(), p.badges(), p.bio(), p.linkUrl(), avg, cnt));
        }
    }

    private static class Review {
        private final Long revId;
        private final Long tgtUserId;
        private final Long frmUserId;
        private final String frmNick;
        private final int rating;
        private final String content;
        private final String date;
        private final ReviewRelation rel;
        private String statCd;

        private Review(Long revId, Long tgtUserId, Long frmUserId, String frmNick, int rating, String content,
                String date, ReviewRelation rel, String statCd) {
            this.revId = revId;
            this.tgtUserId = tgtUserId;
            this.frmUserId = frmUserId;
            this.frmNick = frmNick;
            this.rating = rating;
            this.content = content;
            this.date = date;
            this.rel = rel;
            this.statCd = statCd;
        }

        public Long revId() { return revId; }
        public Long tgtUserId() { return tgtUserId; }
        public Long frmUserId() { return frmUserId; }
        public String frmNick() { return frmNick; }
        public int rating() { return rating; }
        public String content() { return content; }
        public String date() { return date; }
        public ReviewRelation rel() { return rel; }
        public String statCd() { return statCd; }
    }

    private record Profile(Long userId, String nick, String profLv, Map<String, String> badges,
            String bio, String linkUrl, double revAvg, int revCnt) {
    }
}
