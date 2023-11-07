package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @Min(value = 1, message = "Rate must be at least 1")
        @Max(value = 5, message = "Rate must be at most 5")
        byte rate,
        @NotBlank
        String verdict
) {
}
