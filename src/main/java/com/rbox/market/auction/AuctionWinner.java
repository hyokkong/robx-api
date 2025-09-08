package com.rbox.market.auction;

import java.time.Instant;

/** Winner information once auction ends. */
public class AuctionWinner {
    public enum PayStatus { WAIT, PAID, EXPIRE, CANCEL }

    private final Long auctionId;
    private final Long userId;
    private final long amount;
    private PayStatus payStatus = PayStatus.WAIT;
    private final Instant payDue;
    private Instant paidAt;
    private String txId;

    public AuctionWinner(Long auctionId, Long userId, long amount, Instant payDue) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.amount = amount;
        this.payDue = payDue;
    }

    public Long getAuctionId() { return auctionId; }
    public Long getUserId() { return userId; }
    public long getAmount() { return amount; }
    public PayStatus getPayStatus() { return payStatus; }
    public void setPayStatus(PayStatus payStatus) { this.payStatus = payStatus; }
    public Instant getPayDue() { return payDue; }
    public Instant getPaidAt() { return paidAt; }
    public void setPaidAt(Instant paidAt) { this.paidAt = paidAt; }
    public String getTxId() { return txId; }
    public void setTxId(String txId) { this.txId = txId; }
}
