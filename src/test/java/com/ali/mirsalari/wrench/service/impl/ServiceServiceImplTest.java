package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.ServiceExistException;
import com.ali.mirsalari.wrench.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {
    @Mock
    private ServiceRepository serviceRepository;
    @InjectMocks
    private  ServiceServiceImpl underTest;

    private Service it;
    private Service service;

    @BeforeEach
    void setUp() {
        it = new Service("IT");
        service = new Service(7L,
                "IT",
                100L,
                "IT services",
                it,
                null,
                null);

    }

    @Test
    void itShouldSaveAService() {
        //Arrange
        when(serviceRepository.save(any())).thenReturn(service);
        //Act
        Service tempService = underTest.save(service);
        //Assert
        assertNotNull(tempService);
    }

    @Test
    void itShouldThrowServiceExistException() {
        //Arrange
        when(serviceRepository.findServiceByName(any(String.class))).thenReturn(Optional.ofNullable(service));
        //Act and Assert
        assertThrows(ServiceExistException.class, () -> {
            underTest.save(service);
        });
    }

    @Test
    void itShouldThrowNotFoundException() {
//        //Arrange
        service.setId(null);
        //Act and Assert
        assertThrows(NotFoundException.class, () -> {
            underTest.update(service);
        });
    }
    @Test
    void itShouldUpdateService() {
        //Arrange
        when(serviceRepository.save(any())).thenReturn(service);
        when(serviceRepository.findById(any())).thenReturn(Optional.ofNullable(service));
        service.setId(1L);
        //Act
        Service tempService = underTest.update(service);
        //Assert
        assertNotNull(tempService);
    }
    @Test
    void itShouldUpdateServiceWithoutChecking() {
        //Arrange
        when(serviceRepository.save(any())).thenReturn(service);
        //Act
        Service tempService = underTest.uncheckedUpdate(service);
        //Assert
        assertNotNull(tempService);
    }
    @Test
    void itShouldDeleteServiceById() {
        //Act
        underTest.remove(1L);
        //Assert
        verify(serviceRepository, times(1)).deleteById(any());
    }
    @Test
    void itShouldFindAServiceById() {
        //Arrange
        when(serviceRepository.findById(any())).thenReturn(Optional.ofNullable(service));
        //Act and Assert
        if (underTest.findById(1L).isPresent()) {
            Service tempService = underTest.findById(1L).get();
            assertNotNull(tempService);
        }
    }
    @Test
    void itShouldFindServicesAndSubservices() {
        //Arrange
        when(serviceRepository.findAll()).thenReturn(List.of(service,service,service));
        //Act
        List<Service> services = underTest.findAll();
        //Assert
        assertNotNull(services);
    }
    @Test
    void itShouldFindServices() {
        //Arrange
        when(serviceRepository.findAllServices()).thenReturn(List.of(service,service,service));
        //Act
        List<Service> services = underTest.findAllServices();
        //Assert
        assertNotNull(services);
    }
    @Test
    void itShouldFindSubservices() {
        //Arrange
        when(serviceRepository.findAllSubServices()).thenReturn(List.of(service,service,service));
        //Act
        List<Service> services = underTest.findAllSubServices();
        //Assert
        assertNotNull(services);
    }

}