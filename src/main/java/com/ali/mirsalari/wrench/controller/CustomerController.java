package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.service.CustomerService;
import com.ali.mirsalari.wrench.service.dto.ChangePasswordRequest;
import com.ali.mirsalari.wrench.service.dto.CustomerResponse;
import com.ali.mirsalari.wrench.service.dto.RegisterCustomerRequest;
import com.ali.mirsalari.wrench.service.mapper.CustomerResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {
    private final CustomerResponseMapper customerResponseMapper;
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerResponse> getCustomers() {
        List<Customer> customers = customerService.findAll();
        return customers.stream()
                .map(customerResponseMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
            Customer customer = customerService.save(
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.password());
            return ResponseEntity.ok(customerResponseMapper.toDto(customer));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateCustomer(@Valid
                                            @PathVariable("id") Long id,
                                            @RequestBody RegisterCustomerRequest request) {
            Customer customer = customerService.update(
                    id,
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.password());
            return ResponseEntity.ok(customerResponseMapper.toDto(customer));
    }

    @DeleteMapping(path = "{customerId}")
    public HttpStatus deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.remove(customerId);
        return HttpStatus.OK;
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(customerResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<CustomerResponse> findByEmail(@PathVariable String email) {
        Optional<Customer> customer = customerService.findByEmail(email);
        return customer.map(customerResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
            Customer customer = customerService.changePassword(request.newPassword(), request.oldPassword(), userId);
            return ResponseEntity.ok(customerResponseMapper.toDto(customer));
    }
}
