package com.ali.mirsalari.wrench.controller.dto;

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