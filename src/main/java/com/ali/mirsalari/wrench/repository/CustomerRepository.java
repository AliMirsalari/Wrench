package com.ali.mirsalari.wrench.repository;

import com.ali.mirsalari.wrench.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository
        extends JpaRepository<Customer,Long> {
    Optional<Customer> findCustomerByEmail(String email);

}
