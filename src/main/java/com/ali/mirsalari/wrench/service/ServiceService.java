package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService {

    Service saveSubservice(String name, Long basePrice, String description, Long serviceParentId);

    Service saveServices(String name);


    Service updateSubservice(Long id, String name, Long basePrice, String description, Long serviceParentId);

    Service updateService(Long id, String name);

    Service uncheckedUpdate(Service service);

    void remove(Long id);

    Optional<Service> findById(Long id);

    List<Service> findAll();

    @Transactional
    List<Service> findAllServices ();
    @Transactional
    List<Service> findAllSubservices ();
    @Transactional
    Optional<Service> findByName(String name);


}
