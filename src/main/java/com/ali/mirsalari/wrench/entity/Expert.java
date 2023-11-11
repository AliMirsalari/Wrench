package com.ali.mirsalari.wrench.entity;

import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expert extends User {
    @Column(name = "expert_status")
    @Enumerated(EnumType.STRING)
    private ExpertStatus expertStatus;

    @Column(name = "score")
    private int score;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "service_expert",
            joinColumns = @JoinColumn(name = "expert_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> skills = new HashSet<>();

    @OneToMany(mappedBy = "expert")
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "image")
    private byte[] imageData;

    @OneToMany(mappedBy = "expert")
    @ToString.Exclude
    private Set<Bid> bids = new HashSet<>();
    public Expert(Long id, String firstName, String lastName, String email, String password, byte[] imageData) {
        super(id, firstName, lastName, email, password);
        this.expertStatus = ExpertStatus.NEW;
        this.score = 0;
        this.imageData = imageData;

    }

    public Expert(String firstName, String lastName, String email, String password, byte[] imageData) {
        super(firstName, lastName, email, password);
        this.expertStatus = ExpertStatus.NEW;
        this.score = 0;
        this.imageData = imageData;

    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "Expert{" +
                ", firstName= " + super.getFirstName() +
                ", lastName= " + super.getLastName() +
                ", expertStatus=" + expertStatus +
                ", score=" + score +
                ", skills=" + skills +
                '}';
    }

}
