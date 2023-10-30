package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record RegisterBidRequest(
        @NotBlank
        Long suggestedPrice,
        @NotBlank
        Instant startTime,
        @NotBlank
        Instant endTime,
        @NotBlank
        Long expertId,
        @NotBlank
        Long orderId
) {
}
