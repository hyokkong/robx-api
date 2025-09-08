package com.rbox.market.auction;

import java.time.Instant;

/** Request object for creating auction */
public record AuctionCreateReq(Long objId, String curCd, long minBid, long incStep,
        Instant startAt, Instant endAt, boolean profileOnly) {
}
