package com.ali.mirsalari.wrench.controller.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Customer}
 */
public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long credit
) implements Serializable {
}