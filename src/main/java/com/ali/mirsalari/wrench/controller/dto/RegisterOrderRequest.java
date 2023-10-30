package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterOrderRequest(
        @NotBlank
        String description,
        @NotBlank
        Long suggestedPrice,
        @NotBlank
        String address,
        @NotBlank
        Long serviceId,
        @NotBlank
        Long customerId
) {
}
