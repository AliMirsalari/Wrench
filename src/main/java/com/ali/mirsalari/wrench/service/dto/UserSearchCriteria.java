package com.ali.mirsalari.wrench.service.dto;

import java.util.List;

public record UserSearchCriteria(
    String role,
    String firstName,
    String lastName,
    String email,
    List<Long> skillsId,
    Integer minScore,
    Integer maxScore
) { }
