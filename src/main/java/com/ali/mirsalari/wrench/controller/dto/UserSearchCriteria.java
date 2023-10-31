package com.ali.mirsalari.wrench.controller.dto;

import com.ali.mirsalari.wrench.entity.enumeration.ExpertStatus;

import java.util.List;

public record UserSearchCriteria(
    String role,
    String firstName,
    String lastName,
    String email,
    List<Long> skillsId,
    Integer minScore,
    Integer maxScore,
    ExpertStatus expertStatus
) { }
