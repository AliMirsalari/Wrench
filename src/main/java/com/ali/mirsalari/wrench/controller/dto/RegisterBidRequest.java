package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record RegisterBidRequest(
        Long suggestedPrice,
        Instant startTime,
        Instant endTime,
        Long expertId,
        Long orderId
) {
}
