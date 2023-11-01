package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "unapproved_emails")
public class UnapprovedEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Email
    @NotBlank
    @Column(
            name = "email",
            unique = true)
    private String email;

    @NotBlank
    @Column(name = "token")
    private String token;

    @NotNull
    @Column(name = "expire_date")
    private Instant expireDate = Instant.now().plusSeconds(24 * 60 * 60);

    public UnapprovedEmail(String email, String token) {
        this.email = email;
        this.token = token;
    }
}

