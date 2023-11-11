package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Bid;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public interface BidService{
    Bid save(Long suggestedPrice, Instant startTime, Instant endTime, Long expertId, Long orderId);


    Bid update(Long id, Long suggestedPrice, Instant startTime, Instant endTime, Long orderId, String email);

    Bid updateWithEntity(Bid bid);

    void remove(Long id, String email);

    Bid findById(Long id);

    List<Bid> findAll();


    List<Bid> findRelatedBidsOrderByPriceAsc(Long orderId);

    List<Bid> findRelatedBidsOrderByPriceDesc(Long orderId);

    List<Bid> findRelatedBidsOrderByScoreAsc(Long orderId);

    List<Bid> findRelatedBidsOrderByScoreDesc(Long orderId);

    void selectBid(Long bidId);

    Optional<Bid> findSelectedBid(Long orderId);
}
