package com.ali.mirsalari.wrench.service.dto;

import jakarta.validation.constraints.Email;

public record RegisterCustomerRequest(
        String firstName,
        String lastName,
        @Email String email,
        String password
) {
}
