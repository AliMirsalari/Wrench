package com.ali.mirsalari.wrench.service.dto;

public record RegisterOrderRequest(
        String description,
        Long suggestedPrice,
        String address,
        Long serviceId
) {
}
