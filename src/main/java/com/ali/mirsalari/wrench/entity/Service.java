package com.ali.mirsalari.wrench.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "base_price")
    private Long basePrice;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service serviceParent;

    @OneToMany(mappedBy = "serviceParent")
    private Set<Service> subServices = new HashSet<>();

    @ManyToMany(mappedBy = "skills")
    private List <Expert> experts = new ArrayList<>();

    public Service(String name, Long basePrice, String description, Service serviceParent, List<Expert> experts) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.serviceParent = serviceParent;
        this.experts = experts;
    }

    public Service(String name, Long basePrice, String description, Service serviceParent) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.serviceParent = serviceParent;
    }

    public Service(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                ", serviceParent=" + serviceParent +
                '}';
    }
}
