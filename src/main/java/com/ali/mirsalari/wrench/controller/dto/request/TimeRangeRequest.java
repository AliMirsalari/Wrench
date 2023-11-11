package com.ali.mirsalari.wrench.controller.dto.request;

import java.time.Instant;

public record TimeRangeRequest(
        Instant startTime,
        Instant endTime
) {
}
