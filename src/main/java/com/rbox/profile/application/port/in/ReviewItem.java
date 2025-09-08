package com.rbox.profile.application.port.in;

public record ReviewItem(Long revId, ReviewUser fromUser, int rating, String content,
        String date, ReviewRelation rel) {
}
