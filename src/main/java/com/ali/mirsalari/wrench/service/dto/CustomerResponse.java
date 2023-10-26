package com.ali.mirsalari.wrench.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Customer}
 */
public record CustomerResponse(Long id, String firstName, String lastName,
                               @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$") @Email String email,
                               Long credit) implements Serializable {
}