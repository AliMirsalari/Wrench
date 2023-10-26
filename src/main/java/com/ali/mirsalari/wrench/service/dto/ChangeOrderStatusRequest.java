package com.ali.mirsalari.wrench.service.dto;

public record ChangeOrderStatusRequest(
        Long orderId,
        Long bidId) {
}
