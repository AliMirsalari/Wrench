package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Expert;
import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.service.base.CrudService;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BidService
        extends CrudService<Bid,Long>{
    @Transactional
    List<Bid> findRelatedBidsOrderByPriceAsc(Order order);
    @Transactional
    List<Bid> findRelatedBidsOrderByPriceDesc(Order order);
    @Transactional
    List<Bid> findRelatedBidsOrderByScoreAsc(Order order);
    @Transactional
    List<Bid> findRelatedBidsOrderByScoreDesc(Order order);
    @Transactional
    void selectBid(Bid bid);
    @Transactional
    Optional<Bid> findSelectedBid(Order order);
}
