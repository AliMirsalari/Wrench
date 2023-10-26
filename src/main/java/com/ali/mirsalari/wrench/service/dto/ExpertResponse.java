package com.ali.mirsalari.wrench.service.dto;

import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Expert}
 */
public record ExpertResponse(Long id, String firstName, String lastName,
                             @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") @Email String email,
                             ExpertStatus expertStatus, int score, Set<ServiceDto> skills,
                             List<CommentDto1> comments) implements Serializable {
    /**
     * DTO for {@link com.ali.mirsalari.wrench.entity.Service}
     */
    public record ServiceDto(Long id, String name) implements Serializable {
    }

    /**
     * DTO for {@link com.ali.mirsalari.wrench.entity.Comment}
     */
    public record CommentDto1(Long id, byte rate, String verdict, Long customerId, String customerFirstName,
                              String customerLastName, String customerEmail) implements Serializable {
    }
}