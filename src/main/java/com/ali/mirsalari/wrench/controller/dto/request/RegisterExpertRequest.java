package com.ali.mirsalari.wrench.controller.dto.request;

import com.ali.mirsalari.wrench.annotation.ValidImage;
import com.ali.mirsalari.wrench.util.Constants;
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
        @Pattern(regexp = Constants.PASSWORD_REGEX)
        String password,
        @ValidImage
        MultipartFile image
) {
}
