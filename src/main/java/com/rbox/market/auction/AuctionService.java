package com.rbox.market.auction;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * In-memory auction service implementing basic rules from spec.
 */
@Service
public class AuctionService {
    private final Map<Long, Auction> auctions = new ConcurrentHashMap<>();
    private final Map<Long, AuctionWinner> winners = new ConcurrentHashMap<>();
    private final Clock clock;
    private long seq = 1000;

    public AuctionService(Clock clock) {
        this.clock = clock;
    }

    public synchronized Auction create(Long ownerId, AuctionCreateRequest req) {
        if (req.startAt().isAfter(req.endAt())) {
            throw new IllegalArgumentException("startAt must be before endAt");
        }
        if (req.minBid() <= 0 || req.incStep() <= 0) {
            throw new IllegalArgumentException("bid values must be >0");
        }
        Auction auc = new Auction(++seq, req.objId(), ownerId, req.curCd(), req.minBid(),
                req.incStep(), req.startAt(), req.endAt());
        auctions.put(auc.getId(), auc);
        return auc;
    }

    public List<Auction> list() {
        return new ArrayList<>(auctions.values());
    }

    public Auction get(Long id) {
        return auctions.get(id);
    }

    public synchronized Bid placeBid(Long auctionId, Long userId, long amount) {
        Auction auc = auctions.get(auctionId);
        if (auc == null) throw new IllegalArgumentException("auction not found");
        Instant now = clock.instant();
        if (auc.getStatus() != Auction.Status.ACTV)
            throw new IllegalStateException("auction not active");
        if (now.isBefore(auc.getStartAt()) || now.isAfter(auc.getEndAt()))
            throw new IllegalStateException("auction not in time window");
        Bid current = auc.getHighestBid();
        long minRequired = current == null ? auc.getMinBid() : current.getAmount() + auc.getIncStep();
        if (amount < minRequired) {
            throw new IllegalArgumentException("bid too low");
        }
        Bid bid = new Bid(userId, amount, now);
        auc.addBid(bid);
        return bid;
    }

    public AuctionWinner getWinner(Long auctionId) {
        Auction auc = auctions.get(auctionId);
        if (auc == null) return null;
        finalizeAuctionIfNeeded(auc);
        return winners.get(auctionId);
    }

    private void finalizeAuctionIfNeeded(Auction auc) {
        if (auc.getStatus() == Auction.Status.DONE) return;
        Instant now = clock.instant();
        if (now.isBefore(auc.getEndAt())) return; // not yet ended
        Bid highest = auc.getBids().stream().max(Comparator.comparingLong(Bid::getAmount)).orElse(null);
        auc.setStatus(Auction.Status.DONE);
        if (highest != null) {
            Instant payDue = now.plus(24, ChronoUnit.HOURS); // simple fixed TTL
            winners.put(auc.getId(), new AuctionWinner(auc.getId(), highest.getUserId(), highest.getAmount(), payDue));
        }
    }

    public void markPaid(Long auctionId, Instant paidAt, String txId) {
        AuctionWinner w = winners.get(auctionId);
        if (w == null) throw new IllegalArgumentException("no winner");
        if (w.getPayStatus() != AuctionWinner.PayStatus.WAIT)
            throw new IllegalStateException("cannot pay in current state");
        w.setPayStatus(AuctionWinner.PayStatus.PAID);
        w.setPaidAt(paidAt);
        w.setTxId(txId);
    }

    public void cancel(Long auctionId, String reason) {
        AuctionWinner w = winners.get(auctionId);
        if (w == null) throw new IllegalArgumentException("no winner");
        w.setPayStatus(AuctionWinner.PayStatus.CANCEL);
    }

    /** called by scheduled job to expire payment */
    public void expirePayments() {
        Instant now = clock.instant();
        winners.values().stream()
            .filter(w -> w.getPayStatus() == AuctionWinner.PayStatus.WAIT && now.isAfter(w.getPayDue()))
            .forEach(w -> w.setPayStatus(AuctionWinner.PayStatus.EXPIRE));
    }
}
