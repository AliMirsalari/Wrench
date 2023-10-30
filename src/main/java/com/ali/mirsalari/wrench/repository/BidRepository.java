package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Bid;
import com.ali.mirsalari.wrench.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository
        extends JpaRepository<Bid,Long> {

    @Query("SELECT b FROM Bid b WHERE b.order = :order ORDER BY b.suggestedPrice ASC")
    List<Bid> findRelatedBidsOrderByPriceAsc(@Param("order") Order order);
    @Query("SELECT b FROM Bid b WHERE b.order = :order ORDER BY b.suggestedPrice DESC")
    List<Bid> findRelatedBidsOrderByPriceDesc(@Param("order") Order order);

    @Query("SELECT b FROM Bid b WHERE b.order = :order ORDER BY b.expert.score ASC")
    List<Bid> findRelatedBidsOrderByScoreAsc(@Param("order") Order order);
    @Query("SELECT b FROM Bid b WHERE b.order = :order ORDER BY b.expert.score DESC")
    List<Bid> findRelatedBidsOrderByScoreDesc(@Param("order") Order order);

    @Query("SELECT b FROM Bid b WHERE b.order = :order AND b.selectionDate IS NOT NULL")
    Optional<Bid> findSelectedBid(@Param("order") Order order);


}
