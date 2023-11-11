package com.ali.mirsalari.wrench.controller.dto.request;

import com.ali.mirsalari.wrench.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterCustomerRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = Constants.PASSWORD_REGEX)
        String password
) {
}
