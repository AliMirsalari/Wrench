package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Service;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService {

    Service saveSubservice(String name, Long basePrice, String description, Long serviceParentId);

    Service saveServices(String name);


    Service updateSubservice(Long id, String name, Long basePrice, String description, Long serviceParentId);

    Service updateService(Long id, String name);

    Service updateWithEntity(Service service);

    void remove(Long id);

    Service findById(Long id);

    List<Service> findAll();

    List<Service> findAllServices ();
    List<Service> findAllSubservices ();
    Optional<Service> findByName(String name);


}
