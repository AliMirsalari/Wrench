package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.service.base.CrudService;
import com.ali.mirsalari.wrench.service.base.BaseUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService
        extends BaseUserService<Customer, String> {
    @Transactional
    Customer save(String firstName, String lastName, String email, String password);

    @Transactional
    Customer update(Long id, String firstName, String lastName, String email, String password);

    @Transactional
    Customer uncheckedUpdate(Customer customer);

    @Transactional
    void remove(Long id);

    @Transactional
    Optional<Customer> findById(Long id);

    @Transactional
    List<Customer> findAll();
}
