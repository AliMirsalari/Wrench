package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.service.BidService;
import com.ali.mirsalari.wrench.controller.dto.BidResponse;
import com.ali.mirsalari.wrench.controller.dto.RegisterBidRequest;
import com.ali.mirsalari.wrench.controller.mapper.BidResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"","/"})
    public List<BidResponse> getBids() {
        List<Bid> bids = bidService.findAll();
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('EXPERT')")
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

    @PreAuthorize("hasRole('EXPERT')")
    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateBid(
                                       @PathVariable("id") Long id,
                                       @Valid @RequestBody RegisterBidRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
            Bid bid = bidService.update(
                    id,
                    request.suggestedPrice(),
                    request.startTime(),
                    request.endTime(),
                    request.orderId(),
                    userDetails.getUsername()
            );
            return ResponseEntity.ok(bidResponseMapper.toDto(bid));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EXPERT')")
    @DeleteMapping(path = "{bidId}")
    public ResponseEntity<?> deleteBid(@PathVariable("bidId") Long bidId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        bidService.remove(bidId, userDetails.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<BidResponse> findById(@PathVariable Long id) {
        Optional<Bid> bid = bidService.findById(id);
        return bid.map(bidResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','EXPERT')")
    @GetMapping("orderByPriceAsc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByPriceAsc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByPriceAsc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','EXPERT')")
    @GetMapping("orderByPriceDesc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByPriceDesc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByPriceDesc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','EXPERT')")
    @GetMapping("orderByScoreAsc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByScoreAsc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByScoreAsc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','EXPERT')")
    @GetMapping("orderByScoreDesc/{orderId}")
    public List<BidResponse> findRelatedBidsOrderByScoreDesc(@PathVariable Long orderId) {
        List<Bid> bids = bidService.findRelatedBidsOrderByScoreDesc(orderId);
        return bids.stream()
                .map(bidResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/select")
    public ResponseEntity<?> selectBid(@RequestParam Long bidId) {
            bidService.selectBid(bidId);
            return ResponseEntity.ok("Bid selected successfully.");
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','EXPERT')")
    @GetMapping("/findSelectedBid/{orderId}")
    public ResponseEntity<BidResponse> findSelectedBid(@PathVariable Long orderId) {
        Optional<Bid> bid = bidService.findSelectedBid(orderId);

        return bid.map(bidResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
