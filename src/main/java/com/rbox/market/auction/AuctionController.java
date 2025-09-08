package com.rbox.market.auction;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller exposing minimal auction APIs based on the spec.
 */
@RestController
@RequestMapping("/market/auctions")
@Tag(name = "Auction", description = "경매 관련 API")
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
    @Operation(summary = "경매 생성", description = "새로운 경매를 생성합니다")
    public IdResponse create(@RequestHeader("X-USER-ID") String user,
                             @RequestBody AuctionCreateRequest req) {
        Auction auc = service.create(getUserId(user), req);
        return new IdResponse(auc.getId());
    }

    @GetMapping
    @Operation(summary = "경매 목록", description = "경매 목록을 조회합니다")
    public List<Auction> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "경매 상세 조회", description = "경매 상세 정보를 조회합니다")
    public Auction get(@PathVariable Long id) {
        Auction auc = service.get(id);
        if (auc == null) throw new ResourceNotFound();
        return auc;
    }

    @PostMapping("/{id}/bids")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "입찰", description = "경매에 입찰합니다")
    public Bid placeBid(@RequestHeader("X-USER-ID") String user,
                        @PathVariable Long id, @RequestBody BidRequest req) {
        return service.placeBid(id, getUserId(user), req.amount());
    }

    @GetMapping("/{id}/winner")
    @Operation(summary = "낙찰자 조회", description = "경매의 낙찰자를 조회합니다")
    public AuctionWinner winner(@PathVariable Long id) {
        AuctionWinner w = service.getWinner(id);
        if (w == null) throw new ResourceNotFound();
        return w;
    }

    @PostMapping("/{id}/winner/pay")
    @Operation(summary = "낙찰자 결제 처리", description = "낙찰자의 결제 정보를 처리합니다")
    public void pay(@PathVariable Long id, @RequestBody PayRequest req) {
        Instant paidAt = req.paidAt() != null ? req.paidAt() : clock.instant();
        service.markPaid(id, paidAt, req.txId());
    }

    @PostMapping("/{id}/winner/cancel")
    @Operation(summary = "낙찰 취소", description = "경매 낙찰을 취소합니다")
    public void cancel(@PathVariable Long id, @RequestBody CancelRequest req) {
        service.cancel(id, req.reason());
    }

    record IdResponse(Long id) {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ResourceNotFound extends RuntimeException {}
}
