package com.ali.mirsalari.wrench.controller.dto;

public record CaptchaResponse(
            Integer id,
            String base64
    ) {
    }