package com.rbox.market.auction;

import java.time.Instant;

/** Simple bid record. */
public class Bid {
    private final Long userId;
    private final long amount;
    private final Instant bidAt;

    public Bid(Long userId, long amount, Instant bidAt) {
        this.userId = userId;
        this.amount = amount;
        this.bidAt = bidAt;
    }

    public Long getUserId() { return userId; }
    public long getAmount() { return amount; }
    public Instant getBidAt() { return bidAt; }
}
