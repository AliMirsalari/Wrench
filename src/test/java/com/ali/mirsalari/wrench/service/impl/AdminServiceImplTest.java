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
        admin = new Admin(
                "Ahmad",
                "Babaei",
                "ahmad@gmail.com",
                "df4s4654ASF$#%#",
                0L,
                Instant.now()
        );
    }

    @Test
    void itShouldSaveAnAdmin() {
        //Arrange
        when(adminRepository.save(any())).thenReturn(admin);
        //Act
        Admin tempAdmin = underTest.save(admin);
        //Assert
        assertNotNull(tempAdmin);
    }

    @Test
    void itShouldThrowEmailExistException() {
        //Arrange
        when(adminRepository.findAdminByEmail(any())).thenReturn(Optional.ofNullable(admin));
        //Act and Assert
        assertThrows(DuplicateException.class,() ->{
            underTest.save(admin);
        });
    }
    @Test
    void itShouldThrowNotValidPasswordException() {

        admin.setPassword("AAAA");
        //Act and Assert
        assertThrows(NotValidPasswordException.class,() ->{
            underTest.save(admin);
        });
    }
    @Test
    void itShouldThrowNotValidEmailException() {

        admin.setEmail("AAAA");
        //Act and Assert
        assertThrows(NotValidEmailException.class,() ->{
            underTest.save(admin);
        });
    }

    @Test
    void itShouldUpdateAnAdmin() {
        //Arrange
        when(adminRepository.save(any())).thenReturn(admin);
        //Act
        Admin tempAdmin = underTest.update(admin);
        //Assert
        assertNotNull(tempAdmin);
    }
    @Test
    void itShouldUpdateAnAdminWithoutChecking() {
        //Arrange
        when(adminRepository.save(any())).thenReturn(admin);
        //Act
        Admin tempAdmin = underTest.uncheckedUpdate(admin);
        //Assert
        assertNotNull(tempAdmin);
    }

    @Test
    void itShouldDeleteAdminById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(adminRepository, times(1)).deleteById(any());
    }
    @Test
    void itShouldFindAnAdminById() {
        //Arrange
        when(adminRepository.findById(any())).thenReturn(Optional.ofNullable(admin));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Admin tempAdmin = underTest.findById(1L).get();
            assertNotNull(tempAdmin);
        }
    }

    @Test
    void itShouldFindAnAdminByEmail() {
        //Arrange
        when(adminRepository.findAdminByEmail(any())).thenReturn(Optional.ofNullable(admin));
        //Act and Assert
        if (underTest.findByEmail(admin.getEmail()).isPresent()) {
            Admin tempAdmin = underTest.findByEmail(admin.getEmail()).get();
            assertNotNull(tempAdmin);
        }
    }

    @Test
    void itShouldFindAdmins() {
        //Arrange
        when(adminRepository.findAll()).thenReturn(List.of(admin,admin,admin));
        //Act
        List<Admin> admins = underTest.findAll();
        //Assert
        assertNotNull(admins);
    }

    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        admin.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.changePassword(admin.getPassword(), admin);
        });
    }

    @Test
    void itShouldThrowNotValidPasswordExceptionWhenNewPasswordIsNotValid() {
        //Arrange
        when(adminRepository.findById(any())).thenReturn(Optional.ofNullable(admin));
        admin.setId(1L);
        //Act and Assert
        assertThrows(NotValidPasswordException.class, () -> {
            underTest.changePassword("aaaa", admin);
        });
    }
    @Test
    void itShouldChangeThePassword() {
        //Arrange
        when(adminRepository.findById(any())).thenReturn(Optional.ofNullable(admin));
        admin.setId(1L);
        //Act
        String newPassword = "sdf54s]64SFDS%%#";
        underTest.changePassword(newPassword, admin);
//      Assert
        assertEquals(newPassword, admin.getPassword());
    }

}