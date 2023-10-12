package com.ali.mirsalari.wrench.entity;

import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import com.ali.mirsalari.wrench.exception.NotValidImageException;
import com.ali.mirsalari.wrench.util.ImageLoader;
import com.ali.mirsalari.wrench.util.Validator;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
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
public class Expert extends User {
    @Column(name = "expert_status")
    @Enumerated(EnumType.STRING)
    private ExpertStatus expertStatus;

    @Column(name = "score")
    private int score;

    @ManyToMany
    @JoinTable(
            name = "service_expert",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "expert_id")
    )
    private Set<Service> skills = new HashSet<>();

    @OneToMany(mappedBy = "expert")
    private List<Comment> comments = new ArrayList<>();

    @Lob
    @Column(name = "image")
    private byte[] imageData;

    public Expert(String firstName, String lastName, String email, String password, Long credit, Instant registerTime, int score, Set<Service> skills, List<Comment> comments, byte[] imageData) {
        super(firstName, lastName, email, password, credit, registerTime);
        this.expertStatus = ExpertStatus.NEW;
        this.score = 0;
        this.skills = skills;
        this.comments = comments;
        this.imageData = imageData;
    }
    public Expert(Long id, String firstName, String lastName, String email, String password, Instant registerTime, String imageAddress) {
        super(id, firstName, lastName, email, password, registerTime);
        this.expertStatus = ExpertStatus.NEW;
        this.score = 0;
        setImageData(imageAddress);

    }

    public Expert(String firstName, String lastName, String email, String password, Instant registerTime, String imageAddress) {
        super(firstName, lastName, email, password, registerTime);
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
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
