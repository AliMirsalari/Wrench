package com.ali.mirsalari.wrench.service.dto;
import lombok.Getter;

public record ChangePasswordRequest(
        String newPassword,
        String oldPassword) {

}
