package com.ali.mirsalari.wrench.controller.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

public record PaymentRequest(
        int captchaId,
        Long orderId,
        @Size(min = 16, max = 16, message = "Credit card number must be 16 digits long")
        @Pattern(regexp = "^\\d{16}$", message = "Credit card number must be a 16-digit number")
        String cardNumber,
        Long price,
        @Digits(integer = 4, fraction = 0, message = "CVV2 must be a 3 or 4-digit number")
        @Range(min = 100, max = 9999, message = "CVV2 must be between 100 and 9999")
        int cvv2,
        @Digits(integer = 2, fraction = 0, message = "Year must be a 2-digit number")
        @Range(min = 0, max = 99, message = "Year must be between 00 and 99")
        int year,
        @Digits(integer = 2, fraction = 0, message = "Month must be a 2-digit number")
        @Range(min = 1, max = 12, message = "Month must be between 01 and 12")
        int month,
        @NotBlank(message = "Second password is required")
        String secondPassword,
        @NotBlank(message = "Captcha is required")
        String captcha
) {
}
