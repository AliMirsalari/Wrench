package com.ali.mirsalari.wrench.controller.dto;

import java.time.Instant;

public record RegisterBidRequest(
        Long suggestedPrice,
        Instant startTime,
        Instant endTime,
        Long expertId,
        Long orderId
) {
}
