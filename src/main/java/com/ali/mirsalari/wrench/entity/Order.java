package com.ali.mirsalari.wrench.entity;

import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "suggested_price")
    private Long suggestedPrice;

    @Column(name = "date_of_execution")
    private Instant dateOfExecution;

    @Column(name = "address")
    private String address;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<Bid> bids= new ArrayList<>();

    @OneToOne
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order(String description, Long suggestedPrice, Instant dateOfExecution, String address, Service service) {
        this.description = description;
        this.suggestedPrice = suggestedPrice;
        this.dateOfExecution = dateOfExecution;
        this.address = address;
        this.service = service;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", suggestedPrice=" + suggestedPrice +
                ", dateOfExecution=" + dateOfExecution +
                ", address='" + address + '\'' +
                ", orderStatus=" + orderStatus +
                ", service=" + service +
                '}';
    }
}
