package com.ali.mirsalari.wrench.service.dto;

import java.time.Instant;

public record RegisterCommentRequest(
        byte rate,
        String verdict,
        Long customerId,
        Long expertId
) {
}
