package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%^&+=])(?=\\S+$).{8,}$")
        String newPassword,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%^&+=])(?=\\S+$).{8,}$")
        String oldPassword) {

}
