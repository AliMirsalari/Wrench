package com.ali.mirsalari.wrench.service.base;

import java.util.Optional;

public interface BaseUserService<T, EMAIL>{
    Optional<T> findByEmail (EMAIL email);
    T changePassword(String newPassword, String oldPassword, Long userId);
}
