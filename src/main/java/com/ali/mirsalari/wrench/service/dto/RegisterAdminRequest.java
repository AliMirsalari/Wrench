package com.ali.mirsalari.wrench.service.dto;

public record RegisterAdminRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
