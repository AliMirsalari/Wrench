package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository
        extends JpaRepository<Bid,Long> {
}
