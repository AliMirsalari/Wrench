package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterOrderRequest(
        @NotBlank
        String description,
        Long suggestedPrice,
        @NotBlank
        String address,
        Long serviceId,
        Long customerId
) {
}
