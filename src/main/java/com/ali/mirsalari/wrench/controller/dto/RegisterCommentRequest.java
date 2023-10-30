package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterCommentRequest(
        @NotBlank(message = "Rate is required")
        @Pattern(regexp = "^[1-5]$", message = "Rate must be a single digit between 1 and 5")
        byte rate,
        @NotBlank
        String verdict,
        @NotBlank
        Long customerId,
        @NotBlank
        Long expertId
) {
}
