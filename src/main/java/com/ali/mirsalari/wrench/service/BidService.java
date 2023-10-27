package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Bid;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public interface BidService{
    @Transactional
    Bid save(Long suggestedPrice, Instant startTime, Instant endTime, Long expertId, Long orderId);


    @Transactional
    Bid update(Long id, Long suggestedPrice, Instant startTime, Instant endTime, Long expertId, Long orderId);

    @Transactional
    Bid uncheckedUpdate(Bid bid);

    @Transactional
    void remove(Long id);

    @Transactional
    Optional<Bid> findById(Long id);

    @Transactional
    List<Bid> findAll();


    @Transactional
    List<Bid> findRelatedBidsOrderByPriceAsc(Long orderId);

    @Transactional
    List<Bid> findRelatedBidsOrderByPriceDesc(Long orderId);

    @Transactional
    List<Bid> findRelatedBidsOrderByScoreAsc(Long orderId);

    @Transactional
    List<Bid> findRelatedBidsOrderByScoreDesc(Long orderId);

    @Transactional
    void selectBid(Long bidId);

    @Transactional
    Optional<Bid> findSelectedBid(Long orderId);
}
