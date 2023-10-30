package com.ali.mirsalari.wrench.controller.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Service}
 */
public record ServiceResponse(
        Long id,
        String name,
        Long basePrice,
        String description,
        Long serviceParentId,
        String serviceParentName,
        Long serviceParentBasePrice,
        String serviceParentDescription
) implements Serializable {
}