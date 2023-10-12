package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.exception.EmailExistException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import com.ali.mirsalari.wrench.service.CustomerService;
import com.ali.mirsalari.wrench.util.Validator;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer save(Customer customer) throws EmailExistException, NotValidPasswordException, NotValidEmailException {
        if (findByEmail(customer.getEmail()).isPresent()) {
            throw new EmailExistException("Email already exists");
        }
        if (!Validator.isValidPassword(customer.getPassword())) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        if (!Validator.isValidEmail(customer.getEmail())) {
            throw new NotValidEmailException("Email is not good!");
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer update(Customer customer) throws NotFoundException, NotValidPasswordException, NotValidEmailException {
        if (customer.getId() == null || customerRepository.findById(customer.getId()).isEmpty()) {
            throw new NotFoundException("Customer with id: " + customer.getId() + " is not found.");
        }
        if (!Validator.isValidPassword(customer.getPassword())) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        if (!Validator.isValidEmail(customer.getEmail())) {
            throw new NotValidEmailException("Email is not good!");
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    @Override
    @Transactional
    public Optional<Customer> findByEmail(String email) {
            return customerRepository.findCustomerByEmail(email);
    }

    @Override
    @Transactional
    public Customer changePassword(String newPassword, Customer customer) throws NotFoundException, NotValidPasswordException, NotValidEmailException {
        if (customer.getId() == null || customerRepository.findById(customer.getId()).isEmpty()) {
            throw new NotFoundException("Customer with id: " + customer.getId() + " is not found.");
        }
        if (!Validator.isValidPassword(newPassword)) {
            throw new NotValidPasswordException("Password is not valid!");
        }
        customer.setPassword(newPassword);
        return update(customer);
    }
}
