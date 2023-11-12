package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Admin;
import com.ali.mirsalari.wrench.exception.NotValidPasswordException;
import com.ali.mirsalari.wrench.repository.AdminRepository;
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
class AdminServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AdminServiceImpl underTest;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Admin admin;
    private String bcryptPassword;

    @BeforeEach
    void setUp() {
        firstName = "Seyyed Ali";
        lastName = "Mirsalari";
        email = "alimirsalari@outlook.com";
        password = "dfs456SDFS%#$";
        bcryptPassword = "$2a$12$ATUfejDerhp2LeDLbp1lAO2Px0W5vduUM770NV1GIRcKx/5DaTNMa";
        admin = new Admin(firstName, lastName, email, password);
    }

    @Test
    void itShouldSaveAdmin() {
        //Arrange
        when(adminRepository.save(any())).thenReturn(admin);
        //Act
        Admin savedAdmin = underTest.save(firstName, lastName, email, password);
        //Assert
        assertNotNull(savedAdmin);
        assertEquals(admin.getFirstName(), savedAdmin.getFirstName());
        assertEquals(admin.getLastName(), savedAdmin.getLastName());
        assertEquals(admin.getEmail(), savedAdmin.getEmail());
        assertEquals(admin.getPassword(), savedAdmin.getPassword());
        verify(adminRepository, times(1)).save(any());
    }

    @Test
    void itShouldUpdateAdmin() {
        //Arrange
        admin.setId(1L);
        when(userDetails.getUsername()).thenReturn(email);
        when(adminRepository.findAdminByEmail(email)).thenReturn(Optional.ofNullable(admin));
        when(passwordEncoder.encode(password)).thenReturn(bcryptPassword);
        when(adminRepository.save(any())).thenReturn(admin);

        String newFirstName = "Reza";
        String newLastName = "Ahmadi";
        String newEmail = "reza@gmail.com";

        //Act
        Admin updatedAdmin = underTest.update(newFirstName, newLastName, newEmail, password, userDetails);
        //Assert
        assertEquals(newFirstName, updatedAdmin.getFirstName());
        assertEquals(newLastName, updatedAdmin.getLastName());
        assertEquals(newEmail, updatedAdmin.getEmail());
        assertEquals(bcryptPassword, updatedAdmin.getPassword());
    }

    @Test
    void itShouldUpdateAdminWithEntity() {
        //Arrange
        when(adminRepository.save(any())).thenReturn(admin);
        //Act
        underTest.updateWithEntity(admin);
        //Assert
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void itShouldRemoveAdmin() {
        //Act
        underTest.remove(any());
        //Assert
        verify(adminRepository, times(1)).deleteByEmail(any());
    }

    @Test
    void itShouldFindAdminById() {
        //Arrange
        when(adminRepository.findById(any())).thenReturn(Optional.ofNullable(admin));
        //Act
        underTest.findById(any());
        //Assert
        verify(adminRepository, times(1)).findById(any());
    }

    @Test
    void itShouldFindAllAdmins() {
        //Arrange
        when(adminRepository.findAll()).thenReturn(List.of(admin));
        //Act
        underTest.findAll();
        //Assert
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    void itShouldThrowNotValidPasswordException() {
        //Arrange
        when(adminRepository.findAdminByEmail(any())).thenReturn(Optional.ofNullable(admin));
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
        when(adminRepository.findAdminByEmail(any())).thenReturn(Optional.ofNullable(admin));
        String newPassword = "456fdsf$^^$H";
        //Act
        underTest.changePassword(newPassword,
                password,
                email);
        //Assert
        verify(adminRepository, times(1)).findAdminByEmail(email);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(adminRepository, times(1)).save(admin);
    }
}