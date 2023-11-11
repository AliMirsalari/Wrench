package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.service.base.BaseUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService
        extends BaseUserService<Customer, String> {
    Customer save(String firstName, String lastName, String email, String password);

    Customer update(String firstName, String lastName, String email, String password, UserDetails userDetails);

    Customer updateWithEntity(Customer customer);

    void remove(Long id);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();
}
