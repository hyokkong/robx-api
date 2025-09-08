package com.rbox.market.auction;

import java.time.Instant;

/** Request for marking payment */
public record PayReq(Instant paidAt, String txId) {}
