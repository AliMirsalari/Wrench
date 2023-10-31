package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeOrderStatusRequest(
        Long orderId,
        Long bidId) {
}
