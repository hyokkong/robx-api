package com.rbox.market.auction;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Job that marks WAIT winners as EXPIRE after payDue. */
@Component
public class PaymentExpiryJob {
    private final AuctionService service;

    public PaymentExpiryJob(AuctionService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 600_000) // every 10 minutes
    public void run() {
        service.expirePayments();
    }
}
