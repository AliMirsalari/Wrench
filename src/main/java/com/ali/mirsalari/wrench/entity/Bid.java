package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @Column(name = "end_time")
    private Instant endTime;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "selection_date")
    private Instant selectionDate;

    public Bid(Long suggestedPrice, Instant startTime, Instant endTime, Expert expert, Order order) {
        this.bidTime = Instant.now();
        this.suggestedPrice = suggestedPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expert = expert;
        this.order = order;
    }
    public Bid(Long id, Long suggestedPrice, Instant startTime, Instant endTime, Expert expert, Order order) {
        this.id = id;
        this.bidTime = Instant.now();
        this.suggestedPrice = suggestedPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expert = expert;
        this.order = order;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Bid bid = (Bid) o;
        return getId() != null && Objects.equals(getId(), bid.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
