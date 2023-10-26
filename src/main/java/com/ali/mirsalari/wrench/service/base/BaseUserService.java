package com.ali.mirsalari.wrench.service.base;

import jakarta.transaction.Transactional;

import java.util.Optional;

public interface BaseUserService<T, EMAIL>{
    @Transactional
    Optional<T> findByEmail (EMAIL email);
    @Transactional
    T changePassword(String newPassword, String oldPassword, Long userId);
    @Transactional
    void validation(T t);

}
