package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();

    public Customer(String firstName, String lastName, String email, String password, Set<Comment> comments) {
        super(firstName, lastName, email, password);
        this.comments = comments;
    }

    public Customer(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

}
