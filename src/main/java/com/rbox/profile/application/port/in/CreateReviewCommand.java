package com.rbox.profile.application.port.in;

public record CreateReviewCommand(int rating, String content, Long objId,
        Long listingId, Long auctionId) {
}
