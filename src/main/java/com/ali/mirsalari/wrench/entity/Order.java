package com.ali.mirsalari.wrench.entity;

import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
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
    @ToString.Exclude
    private List<Bid> bids= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order(String description, Long suggestedPrice, Instant dateOfExecution, String address, Service service, Customer customer) {
        this.description = description;
        this.suggestedPrice = suggestedPrice;
        this.dateOfExecution = dateOfExecution;
        this.address = address;
        this.service = service;
        this.customer = customer;
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
