package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SkillRequest(
        @NotBlank
        Long skillId,
        @NotBlank
        Long expertId
) {
}
