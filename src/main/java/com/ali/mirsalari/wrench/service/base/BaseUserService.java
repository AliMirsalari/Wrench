package com.ali.mirsalari.wrench.service.base;

import java.util.Optional;

public interface BaseUserService<T, EMAIL>{
    T findByEmail (EMAIL email);
    T changePassword(String newPassword, String oldPassword, String email);
}
