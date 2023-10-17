package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

    public Customer(String firstName, String lastName, String email, String password, Long credit, Instant registerTime, Set<Comment> comments) {
        super(firstName, lastName, email, password, credit, registerTime);
        this.comments = comments;
    }

    public Customer(String firstName, String lastName, String email, String password, Instant registerTime) {
        super(firstName, lastName, email, password, registerTime);
    }
}
