package com.ali.mirsalari.wrench.entity;

import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import com.ali.mirsalari.wrench.exception.FileException;
import com.ali.mirsalari.wrench.exception.NotValidImageException;
import com.ali.mirsalari.wrench.util.ImageLoader;
import com.ali.mirsalari.wrench.util.Validator;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Slf4j
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
    public Expert(Long id, String firstName, String lastName, String email, String password, String imageAddress) {
        super(id, firstName, lastName, email, password);
        this.expertStatus = ExpertStatus.NEW;
        this.score = 0;
        setImageData(imageAddress);

    }

    public Expert(String firstName, String lastName, String email, String password, String imageAddress) {
        super(firstName, lastName, email, password);
        this.expertStatus = ExpertStatus.NEW;
        this.score = 0;
        setImageData(imageAddress);

    }

    public void setImageData(String imageAddress) {
        try {
            File imageFile = new File(imageAddress);
            if (Validator.isValidImage(imageFile)){
                this.imageData = ImageLoader.loadImageBytes(imageAddress);
            }else{
                throw new NotValidImageException("Image is not valid");
            }
        } catch (IOException e) {
            log.error("There is an error loading the image file!");
            throw new FileException("There is an error loading the image file!");
        }
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
