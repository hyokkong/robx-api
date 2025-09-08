package com.rbox.market.auction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Simplified in-memory Auction entity.
 */
public class Auction {
    public enum Status { ACTV, DONE, CANC }

    private Long id;
    private Long objId;
    private Long ownerId;
    private String currency;
    private long minBid;
    private long incStep;
    private Instant startAt;
    private Instant endAt;
    private Status status = Status.ACTV;

    private List<Bid> bids = new ArrayList<>();

    public Auction(Long id, Long objId, Long ownerId, String currency, long minBid, long incStep,
                   Instant startAt, Instant endAt) {
        this.id = id;
        this.objId = objId;
        this.ownerId = ownerId;
        this.currency = currency;
        this.minBid = minBid;
        this.incStep = incStep;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Long getId() { return id; }
    public Long getObjId() { return objId; }
    public Long getOwnerId() { return ownerId; }
    public String getCurrency() { return currency; }
    public long getMinBid() { return minBid; }
    public long getIncStep() { return incStep; }
    public Instant getStartAt() { return startAt; }
    public Instant getEndAt() { return endAt; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Bid> getBids() { return bids; }
    public Bid getHighestBid() {
        return bids.stream().max((a,b) -> Long.compare(a.getAmount(), b.getAmount())).orElse(null);
    }

    public void addBid(Bid b) { bids.add(b); }
}
