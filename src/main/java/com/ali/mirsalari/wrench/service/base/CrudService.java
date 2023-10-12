package com.ali.mirsalari.wrench.service.base;

import com.ali.mirsalari.wrench.exception.*;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    T save (T t) throws ServiceExistException, EmailExistException, NotValidPasswordException, NotValidEmailException, NotValidPriceException, NotValidTimeException;
    T update (T t) throws NotFoundException, NotValidPasswordException, NotValidEmailException, NotValidPriceException, NotValidTimeException;
    void remove (ID id);
    Optional<T> findById (ID id);
    List<T> findAll ();
}
