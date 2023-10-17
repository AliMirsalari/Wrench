package com.ali.mirsalari.wrench.service;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.ServiceExistException;
import com.ali.mirsalari.wrench.service.base.CrudService;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService
        extends CrudService<Service, Long> {
    @Transactional
    List<Service> findAllServices ();
    @Transactional
    List<Service> findAllSubServices ();
    @Transactional
    Optional<Service> findByName(String name);


}
