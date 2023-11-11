package com.ali.mirsalari.wrench.controller.dto.response;

import com.ali.mirsalari.wrench.entity.enumeration.OrderStatus;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Order}
 */
public record OrderResponse(
        Long id,
        String description,
        Long suggestedPrice,
        Instant dateOfExecution,
        String address,
        OrderStatus orderStatus,
        Long serviceId,
        String serviceName,
        Long serviceBasePrice,
        String serviceDescription,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        String customerEmail
) implements Serializable {
}