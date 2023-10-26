package com.ali.mirsalari.wrench.service.dto;

import java.time.Instant;

public record RegisterBidRequest(
        Long suggestedPrice,
        Instant startTime,
        Instant endTime,
        Long expertId,
        Long orderId
) {
}
