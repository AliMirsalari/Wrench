package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.User}
 */
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email
) implements Serializable {
}