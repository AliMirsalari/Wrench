package com.ali.mirsalari.wrench.service.base;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;

import java.util.Optional;

public interface UserService<T, EMAIL>{
    Optional<T> findByEmail (EMAIL email);
    T changePassword(String newPassword, T t) throws NotFoundException, NotValidPasswordException, NotValidEmailException;
    void validation(T t);

}
