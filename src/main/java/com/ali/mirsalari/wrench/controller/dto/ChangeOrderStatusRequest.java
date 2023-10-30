package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeOrderStatusRequest(
        @NotBlank
        Long orderId,
        @NotBlank
        Long bidId) {
}
