package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SkillRequest(
        Long skillId,
        Long expertId
) {
}
