package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import com.ali.mirsalari.wrench.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer save(String firstName, String lastName, String email, String password) {
        Customer customer = new Customer(firstName, lastName, email, passwordEncoder.encode(password));
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(String firstName, String lastName, String email, String password, UserDetails userDetails) {
        Customer customer = findByEmail(userDetails.getUsername());
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        return customerRepository.save(customer);
    }
    @Override
    public Customer updateWithEntity(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return  customerRepository.save(customer);
    }
    @Override
    public void remove(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with ID " + id + " is not found."));
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    @Override
    public Customer findByEmail(String email) {
            return customerRepository.findCustomerByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Customer with Email " + email + " is not found."));
    }

    @Override
    public Customer changePassword(String newPassword, String oldPassword, String email) {
        Customer customer = findByEmail(email);
        if (!Objects.equals(customer.getPassword(), oldPassword)) {
            throw new NotValidPasswordException("The entered password is not the same as the password!");
        }
        customer.setPassword(passwordEncoder.encode(newPassword));
        return updateWithEntity(customer);
    }
}
