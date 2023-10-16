package com.ali.mirsalari.wrench.service.base;

import com.ali.mirsalari.wrench.exception.*;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T save (T t);
    T update (T t);
    void remove (ID id);
    Optional<T> findById (ID id);
    List<T> findAll ();
}
