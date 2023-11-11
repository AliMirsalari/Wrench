package com.ali.mirsalari.wrench.controller.dto.response;

import java.io.Serializable;

/**
 * DTO for {@link com.ali.mirsalari.wrench.entity.Comment}
 */
public record CommentResponse(
        Long id,
        byte rate,
        String verdict,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        Long expertId,
        String expertFirstName,
        String expertLastName,
        int expertScore
) implements Serializable {
}