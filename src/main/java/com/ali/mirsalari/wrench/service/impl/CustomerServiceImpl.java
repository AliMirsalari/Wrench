package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import com.ali.mirsalari.wrench.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer save(String firstName, String lastName, String email, String password) {
        Customer customer = new Customer(firstName, lastName, email, password);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, String firstName, String lastName, String email, String password) {
        Customer customer = findById(id).orElseThrow(()->new NotFoundException("Customer is not found!"));
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPassword(password);
        return customerRepository.save(customer);
    }
    @Override
    public Customer updateWithEntity(Customer customer) {
        return  customerRepository.save(customer);
    }
    @Override
    public void remove(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    @Override
    public Optional<Customer> findByEmail(String email) {
            return customerRepository.findCustomerByEmail(email);
    }

    @Override
    public Customer changePassword(String newPassword, String oldPassword, Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Customer with ID " + userId + " is not found."));
        if (!Objects.equals(customer.getPassword(), oldPassword)) {
            throw new NotValidPasswordException("The entered password is not the same as the password!");
        }
        customer.setPassword(newPassword);
        return updateWithEntity(customer);
    }
}
