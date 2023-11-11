package com.ali.mirsalari.wrench.controller.dto.request;

import com.ali.mirsalari.wrench.util.Constants;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest(
        @Pattern(regexp = Constants.PASSWORD_REGEX)
        String newPassword,
        @Pattern(regexp = Constants.PASSWORD_REGEX)
        String confirmNewPassword,
        @Pattern(regexp = Constants.PASSWORD_REGEX)
        String oldPassword) {

}
