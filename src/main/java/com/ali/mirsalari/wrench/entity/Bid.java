package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "bid_time")
    private Instant bidTime;

    @Column(name = "suggested_price")
    private Long suggestedPrice;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "duration")
    private Instant duration;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Bid(Instant bidTime, Long suggestedPrice, Instant startTime, Instant duration, Order order) {
        this.bidTime = bidTime;
        this.suggestedPrice = suggestedPrice;
        this.startTime = startTime;
        this.duration = duration;
        this.order = order;
    }
}
