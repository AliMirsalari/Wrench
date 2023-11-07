package com.ali.mirsalari.wrench.controller.dto;

public record ChangeOrderStatusRequest(
        Long orderId,
        Long bidId) {
}
