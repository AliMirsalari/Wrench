package com.ali.mirsalari.wrench.service.base;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.exception.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    @Transactional
    T save (T t);
    @Transactional
    T update (T t);
    @Transactional
    T uncheckedUpdate(T t);
    @Transactional
    void remove (ID id);
    @Transactional
    Optional<T> findById (ID id);
    @Transactional
    List<T> findAll ();
}
