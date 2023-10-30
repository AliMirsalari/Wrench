package com.ali.mirsalari.wrench.controller.dto;

import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Bid}
 */
public record BidResponse(
        Long id,
        Instant bidTime,
        Long suggestedPrice,
        Instant startTime,
        Instant endTime,
        Long expertId,
        String expertFirstName,
        String expertLastName,
        int expertScore,
        Long orderId,
        String orderDescription,
        Long orderSuggestedPrice,
        Instant orderDateOfExecution,
        String orderAddress,
        OrderStatus orderOrderStatus,
        Instant selectionDate
) implements Serializable {
}