package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.controller.dto.request.ChangePasswordRequest;
import com.ali.mirsalari.wrench.controller.dto.response.CustomerResponse;
import com.ali.mirsalari.wrench.controller.dto.request.RegisterCustomerRequest;
import com.ali.mirsalari.wrench.controller.mapper.CustomerResponseMapper;
import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customer")
public class CustomerController {
    private final CustomerResponseMapper customerResponseMapper;
    private final CustomerService customerService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"", "/"})
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


    @PutMapping(path = "/updateExpert")
    public ResponseEntity<?> updateCustomer(
            @Valid @RequestBody RegisterCustomerRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.update(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.password(),
                userDetails);
        return ResponseEntity.ok(customerResponseMapper.toDto(customer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") Long customerId) {
        customerService.remove(customerId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
        Optional<Customer> customer = Optional.ofNullable(customerService.findById(id));
        return customer.map(customerResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<CustomerResponse> findByEmail(@PathVariable String email) {
        Optional<Customer> customer = Optional.ofNullable(customerService.findByEmail(email));
        return customer.map(customerResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody ChangePasswordRequest request) {
        if (!Objects.equals(request.newPassword(), request.confirmNewPassword())){
            throw new  IllegalArgumentException("Passwords must be the same!");
        }
        Customer customer = customerService.changePassword(request.newPassword(),
                request.oldPassword(),
                userDetails.getUsername());
        return ResponseEntity.ok(customerResponseMapper.toDto(customer));
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/getRegisterDate/{id}")
    public ResponseEntity<String> getRegisterDate(@PathVariable Long id) {
        Optional<Customer> customerOptional = Optional.ofNullable(customerService.findById(id));

        return customerOptional.map(customer -> {
            Instant registerTime = customer.getRegisterTime();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(registerTime, zoneId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zoneId);
            String formattedTime = formatter.format(zonedDateTime);

            return ResponseEntity.ok(formattedTime);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/getCreditAmount")
    public ResponseEntity<?> getCreditAmount(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<Customer> customerOptional = Optional.ofNullable(customerService.findByEmail(userDetails.getUsername()));

        return customerOptional.map(customer -> ResponseEntity.ok(customer.getCredit()))
                .orElse(ResponseEntity.notFound().build());
    }
}
