package com.ali.mirsalari.wrench.controller.dto.request;

public record ChangeOrderStatusRequest(
        Long orderId,
        Long bidId) {
}
