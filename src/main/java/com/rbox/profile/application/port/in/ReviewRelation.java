package com.rbox.profile.application.port.in;

public record ReviewRelation(String type, Long auctionId, Long listingId, Long objId) {
}
