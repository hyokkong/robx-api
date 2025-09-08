package com.rbox.market.auction;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing minimal auction APIs based on the spec.
 */
@RestController
@RequestMapping("/market/auctions")
public class AuctionController {
    private final AuctionService service;
    private final Clock clock;

    public AuctionController(AuctionService service, Clock clock) {
        this.service = service;
        this.clock = clock;
    }

    private Long getUserId(String header) {
        if (header == null) return null;
        return Long.valueOf(header);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse create(@RequestHeader("X-USER-ID") String user,
                             @RequestBody AuctionCreateRequest req) {
        Auction auc = service.create(getUserId(user), req);
        return new IdResponse(auc.getId());
    }

    @GetMapping
    public List<Auction> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Auction get(@PathVariable Long id) {
        Auction auc = service.get(id);
        if (auc == null) throw new ResourceNotFound();
        return auc;
    }

    @PostMapping("/{id}/bids")
    @ResponseStatus(HttpStatus.CREATED)
    public Bid placeBid(@RequestHeader("X-USER-ID") String user,
                        @PathVariable Long id, @RequestBody BidRequest req) {
        return service.placeBid(id, getUserId(user), req.amount());
    }

    @GetMapping("/{id}/winner")
    public AuctionWinner winner(@PathVariable Long id) {
        AuctionWinner w = service.getWinner(id);
        if (w == null) throw new ResourceNotFound();
        return w;
    }

    @PostMapping("/{id}/winner/pay")
    public void pay(@PathVariable Long id, @RequestBody PayRequest req) {
        Instant paidAt = req.paidAt() != null ? req.paidAt() : clock.instant();
        service.markPaid(id, paidAt, req.txId());
    }

    @PostMapping("/{id}/winner/cancel")
    public void cancel(@PathVariable Long id, @RequestBody CancelRequest req) {
        service.cancel(id, req.reason());
    }

    record IdResponse(Long id) {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ResourceNotFound extends RuntimeException {}
}
