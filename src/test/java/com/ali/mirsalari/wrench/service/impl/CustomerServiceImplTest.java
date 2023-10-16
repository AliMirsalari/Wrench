package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Customer;
import com.ali.mirsalari.wrench.exception.DuplicateEmailException;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.NotValidEmailException;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerServiceImpl underTest;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "Hossein",
                "Yazdani",
                "hosein@gmail.com",
                "s56]d4rf4SDFS%#%#",
                0L,
                Instant.now(),
                null
        );
    }


    @Test
    void itShouldSaveAnCustomer() {
        //Arrange
        when(customerRepository.save(any())).thenReturn(customer);
        //Act
        Customer tempCustomer = underTest.save(customer);
        //Assert
        assertNotNull(tempCustomer);
    }

    @Test
    void itShouldThrowEmailExistException() {
        //Arrange
        when(customerRepository.findCustomerByEmail(any())).thenReturn(Optional.ofNullable(customer));
        //Act and Assert
        assertThrows(DuplicateEmailException.class,() ->{
            underTest.save(customer);
        });
    }
    @Test
    void itShouldThrowNotValidPasswordException() {

        customer.setPassword("AAAA");
        //Act and Assert
        assertThrows(NotValidPasswordException.class,() ->{
            underTest.save(customer);
        });
    }
    @Test
    void itShouldThrowNotValidEmailException() {

        customer.setEmail("AAAA");
        //Act and Assert
        assertThrows(NotValidEmailException.class,() ->{
            underTest.save(customer);
        });
    }

    @Test
    void itShouldUpdateAnCustomer() {
        //Arrange
        when(customerRepository.save(any())).thenReturn(customer);
        //Act
        Customer tempCustomer = underTest.update(customer);
        //Assert
        assertNotNull(tempCustomer);
    }

    @Test
    void itShouldDeleteCustomerById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(customerRepository, times(1)).deleteById(1L);
    }
    @Test
    void itShouldFindAnCustomerById() {
        //Arrange
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Customer tempCustomer = underTest.findById(1L).get();
            assertNotNull(tempCustomer);
        }
    }

    @Test
    void itShouldFindAnCustomerByEmail() {
        //Arrange
        when(customerRepository.findCustomerByEmail(any())).thenReturn(Optional.ofNullable(customer));
        //Act and Assert
        if (underTest.findByEmail(customer.getEmail()).isPresent()) {
            Customer tempCustomer = underTest.findByEmail(customer.getEmail()).get();
            assertNotNull(tempCustomer);
        }
    }

    @Test
    void itShouldFindCustomers() {
        //Arrange
        when(customerRepository.findAll()).thenReturn(List.of(customer,customer,customer));
        //Act
        List<Customer> customers = underTest.findAll();
        //Assert
        assertNotNull(customers);
    }

    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        customer.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.changePassword(customer.getPassword(), customer);
        });
    }

    @Test
    void itShouldThrowNotValidPasswordExceptionWhenNewPasswordIsNotValid() {
        //Arrange
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        customer.setId(1L);
        //Act and Assert
        assertThrows(NotValidPasswordException.class, () -> {
            underTest.changePassword("aaaa", customer);
        });
    }
    @Test
    void itShouldChangeThePassword() {
        //Arrange
        when(customerRepository.findById(any())).thenReturn(Optional.ofNullable(customer));
        customer.setId(1L);
        //Act
        String newPassword = "sdf54s]64SFDS%%#";
        underTest.changePassword(newPassword, customer);
//      Assert
        assertEquals(newPassword, customer.getPassword());
    }

}