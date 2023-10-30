package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Admin extends User{
    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

}
