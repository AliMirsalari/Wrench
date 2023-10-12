package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Admin extends User{
    public Admin(String firstName, String lastName, String email, String password, Long credit, Instant registerTime) {
        super(firstName, lastName, email, password, credit, registerTime);
    }
}
