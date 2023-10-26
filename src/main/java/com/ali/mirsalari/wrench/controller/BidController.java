package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.service.BidService;
import com.ali.mirsalari.wrench.service.dto.BidResponse;
import com.ali.mirsalari.wrench.service.dto.RegisterBidRequest;
import com.ali.mirsalari.wrench.service.mapper.BidResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/bid")
public class BidController {
    private final BidResponseMapper bidResponseMapper;
    private final BidService bidService;

    @GetMapping
    public List<BidResponse> getBids() {
        List<Bid> bids = bidService.findAll();
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registerBid(@Valid @RequestBody RegisterBidRequest request) {
            Bid bid = bidService.save(
                    request.suggestedPrice(),
                    request.startTime(),
                    request.endTime(),
                    request.expertId(),
                    request.orderId());
            return ResponseEntity.ok(bidResponseMapper.toDto(bid));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateBid(@Valid
                                       @PathVariable("id") Long id,
                                       @RequestBody RegisterBidRequest request) {
            Bid bid = bidService.update(
                    id,
                    request.suggestedPrice(),
                    request.startTime(),
                    request.endTime(),
                    request.expertId(),
                    request.orderId()
            );
            return ResponseEntity.ok(bidResponseMapper.toDto(bid));
    }
    @DeleteMapping(path = "{bidId}")
    public HttpStatus deleteBid(@PathVariable("bidId") Long bidId) {
        bidService.remove(bidId);
        return HttpStatus.OK;
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<BidResponse> findById(@PathVariable Long id) {
        Optional<Bid> bid = bidService.findById(id);
        return bid.map(bidResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("orderByPriceAsc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByPriceAsc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByPriceAsc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping("orderByPriceDesc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByPriceDesc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByPriceDesc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping("orderByScoreAsc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByScoreAsc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByScoreAsc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping("orderByScoreDesc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByScoreDesc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByScoreDesc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/select")
    public ResponseEntity<?> selectBid(@RequestParam Long bidId) {
            bidService.selectBid(bidId);
            return ResponseEntity.ok("Bid selected successfully.");
    }
    @GetMapping("/findSelectedBid/{orderId}")
    public ResponseEntity<BidResponse> findSelectedBid(@PathVariable Long orderId) {
        Optional<Bid> bid = bidService.findSelectedBid(orderId);

        return bid.map(bidResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
