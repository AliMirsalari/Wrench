package com.ali.mirsalari.wrench.service.base;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface UserService<T, EMAIL>{
    @Transactional
    Optional<T> findByEmail (EMAIL email);
    @Transactional
    T changePassword(String newPassword, T t);
    @Transactional
    void validation(T t);

}
