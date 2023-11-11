package com.ali.mirsalari.wrench.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterServiceRequest(
        @NotBlank
        String name,
        Long basePrice,
        @NotBlank
        String description,
        Long serviceParentId
) {
}
