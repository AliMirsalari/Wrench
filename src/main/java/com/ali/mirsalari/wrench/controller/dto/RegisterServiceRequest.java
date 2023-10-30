package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterServiceRequest(
        @NotBlank
        String name,
        @NotBlank
        Long basePrice,
        @NotBlank
        String description,
        @NotBlank
        Long serviceParentId
) {
}
