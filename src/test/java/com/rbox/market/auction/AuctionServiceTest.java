package com.rbox.market.auction;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuctionServiceTest {
    static class MutableClock extends Clock {
        private Instant instant;
        MutableClock(Instant i) { this.instant = i; }
        void set(Instant i) { this.instant = i; }
        @Override public ZoneId getZone() { return ZoneId.of("UTC"); }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }

    private MutableClock clock;
    private AuctionService service;

    @BeforeEach
    void setup() {
        clock = new MutableClock(Instant.parse("2025-10-01T00:00:00Z"));
        service = new AuctionService(clock);
    }

    @Test
    void createBidWinnerAndPay() {
        AuctionCreateRequest req = new AuctionCreateRequest(1001L, "KRW", 100, 10,
                Instant.parse("2025-10-01T00:00:00Z"), Instant.parse("2025-10-01T12:00:00Z"), false);
        Auction auc = service.create(1L, req);
        service.placeBid(auc.getId(), 2L, 100);
        clock.set(Instant.parse("2025-10-02T00:00:00Z"));
        AuctionWinner w = service.getWinner(auc.getId());
        assertNotNull(w);
        assertEquals(AuctionWinner.PayStatus.WAIT, w.getPayStatus());
        service.markPaid(auc.getId(), clock.instant(), "TX1");
        assertEquals(AuctionWinner.PayStatus.PAID, w.getPayStatus());
    }

    @Test
    void expirePayment() {
        AuctionCreateRequest req = new AuctionCreateRequest(1001L, "KRW", 100, 10,
                Instant.parse("2025-10-01T00:00:00Z"), Instant.parse("2025-10-01T12:00:00Z"), false);
        Auction auc = service.create(1L, req);
        service.placeBid(auc.getId(), 2L, 100);
        clock.set(Instant.parse("2025-10-02T00:00:00Z"));
        AuctionWinner w = service.getWinner(auc.getId());
        clock.set(clock.instant().plusSeconds(60*60*24 + 1)); // just past payDue
        service.expirePayments();
        assertEquals(AuctionWinner.PayStatus.EXPIRE, w.getPayStatus());
    }
}
