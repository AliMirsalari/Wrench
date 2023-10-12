package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "rate")
    private byte rate;

    @Column(name = "verdict")
    private String verdict;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

    public Comment(byte rate, String verdict, Customer customer, Expert expert) {
        this.rate = rate;
        this.verdict = verdict;
        this.customer = customer;
        this.expert = expert;
    }
}
