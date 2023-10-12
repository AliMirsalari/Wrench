package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Customer extends User {

    @OneToMany(mappedBy = "expert")
    private List<Comment> comments = new ArrayList<>();

    public Customer(String firstName, String lastName, String email, String password, Long credit, Instant registerTime, List<Comment> comments) {
        super(firstName, lastName, email, password, credit, registerTime);
        this.comments = comments;
    }

    public Customer(String firstName, String lastName, String email, String password, Instant registerTime) {
        super(firstName, lastName, email, password, registerTime);
    }
}
