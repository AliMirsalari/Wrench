package com.ali.mirsalari.wrench.service.dto;

public record RegisterExpertRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
