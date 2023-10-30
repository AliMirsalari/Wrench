package com.ali.mirsalari.wrench.controller.dto;

import com.ali.mirsalari.wrench.annotation.ValidImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

public record RegisterExpertRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%^&+=])(?=\\S+$).{8,}$")
        String password,
        @ValidImage
        MultipartFile image
) {
}
