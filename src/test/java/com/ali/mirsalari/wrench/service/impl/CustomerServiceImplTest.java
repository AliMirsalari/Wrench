package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CustomerServiceImpl underTest;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bcryptPassword;
    private Customer customer;

    @BeforeEach
    void setUp() {
        firstName = "Seyyed Ali";
        lastName = "Mirsalari";
        email = "alimirsalari@outlook.com";
        password = "dfs456SDFS%#$";
        bcryptPassword = "$2a$12$ATUfejDerhp2LeDLbp1lAO2Px0W5vduUM770NV1GIRcKx/5DaTNMa";
        customer = new Customer(firstName, lastName, email, password);
    }

    @Test
    void itShouldSaveCustomer() {
        //Arrange
        when(customerRepository.save(any())).thenReturn(customer);
        //Act
        Customer savedCustomer = underTest.save(firstName, lastName, email, password);
        //Assert
        assertNotNull(savedCustomer);
        assertEquals(customer.getFirstName(), savedCustomer.getFirstName());
        assertEquals(customer.getLastName(), savedCustomer.getLastName());
        assertEquals(customer.getEmail(), savedCustomer.getEmail());
        assertEquals(customer.getPassword(), savedCustomer.getPassword());
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    void itShouldUpdateCustomer() {
        //Arrange
        customer.setId(1L);
        when(userDetails.getUsername()).thenReturn(email);
        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.ofNullable(customer));
        when(passwordEncoder.encode(password)).thenReturn(bcryptPassword);
        when(customerRepository.save(any())).thenReturn(customer);

        String newFirstName = "Reza";
        String newLastName = "Ahmadi";
        String newEmail = "reza@gmail.com";

        //Act
        Customer updatedCustomer = underTest.update(newFirstName, newLastName, newEmail, password, userDetails);
        //Assert
        assertEquals(newFirstName, updatedCustomer.getFirstName());
        assertEquals(newLastName, updatedCustomer.getLastName());
        assertEquals(newEmail, updatedCustomer.getEmail());
        assertEquals(bcryptPassword, updatedCustomer.getPassword());
    }

    @Test
    void itShouldUpdateCustomerWithEntity() {
        //Arrange
        when(customerRepository.save(any())).thenReturn(customer);
        //Act
        underTest.updateWithEntity(customer);
        //Assert
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void itShouldRemoveCustomer() {
        //Act
        underTest.remove(any());
        //Assert
        verify(customerRepository, times(1)).deleteById(any());
    }

    @Test
    void itShouldFindCustomerById() {
        //Arrange
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        //Act
        underTest.findById(any());
        //Assert
        verify(customerRepository, times(1)).findById(any());
    }

    @Test
    void itShouldFindAllCustomers() {
        //Arrange
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        //Act
        underTest.findAll();
        //Assert
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void itShouldThrowNotValidPasswordException() {
        //Arrange
        when(customerRepository.findCustomerByEmail(any())).thenReturn(Optional.ofNullable(customer));
        //Act and Assert
        assertThrows(NotValidPasswordException.class, () ->
                underTest.changePassword(
                        "d4fs4]56FSD%#",
                        "456fdsf$^^$H",
                        email));
    }

    @Test
    void itShouldChangePassword() {
        //Arrange
        when(customerRepository.findCustomerByEmail(any())).thenReturn(Optional.ofNullable(customer));
        String newPassword = "456fdsf$^^$H";
        //Act
        underTest.changePassword(newPassword,
                password,
                email);
        //Assert
        verify(customerRepository, times(1)).findCustomerByEmail(email);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(customerRepository, times(1)).save(customer);
    }
}