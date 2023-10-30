package com.ali.mirsalari.wrench.controller.dto;

import jakarta.validation.constraints.*;

public record PaymentRequest(
        @NotBlank
        int captchaId,
        @NotBlank
        Long orderId,
        @NotBlank(message = "Credit card number is required")
        @Size(min = 16, max = 16, message = "Credit card number must be 16 digits long")
        @Pattern(regexp = "^\\d{16}$", message = "Credit card number must be a 16-digit number")
        String cardNumber,
        @NotBlank
        Long price,
        @NotBlank(message = "CVV2 is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV2 must be a 3 to 4-digit number")
        int cvv2,
        @NotBlank(message = "Year is required")
        @Pattern(regexp = "^(0[0-9]|[1-9][0-9])$", message = "Year must be a two-digit number between 00 and 99")
        int year,
        @NotBlank(message = "Month is required")
        @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Month must be a two-digit number between 01 and 12")
        int month,
        @NotBlank(message = "Second password is required")
        String secondPassword,
        @NotBlank(message = "Captcha is required")
        String captcha
) {
}
