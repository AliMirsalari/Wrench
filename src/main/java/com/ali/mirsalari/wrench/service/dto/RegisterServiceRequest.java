package com.ali.mirsalari.wrench.service.dto;

public record RegisterServiceRequest(
        String name,
        Long basePrice,
        String description,
        Long serviceParentId
) {
}
