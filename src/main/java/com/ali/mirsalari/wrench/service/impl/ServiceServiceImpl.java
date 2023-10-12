package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Order;
import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.ServiceExistException;
import com.ali.mirsalari.wrench.repository.ServiceRepository;
import com.ali.mirsalari.wrench.service.ServiceService;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;


    @Override
    @Transactional
    public Service save(Service service) throws ServiceExistException {
        if (findByName(service.getName()).isPresent()) {
            throw new ServiceExistException("Service " + service.getName() + " already exists");
        }
        return serviceRepository.save(service);

    }
    @Override
    @Transactional
    public Service update(Service service) throws NotFoundException {
        if (service.getId() == null || serviceRepository.findById(service.getId()).isEmpty()) {
            throw new NotFoundException("Service with id: " + service.getId() + " is not found.");
        }
        return serviceRepository.save(service);
    }
    @Override
    @Transactional
    public void remove(Long id) {
        serviceRepository.deleteById(id);
    }
    @Override
    @Transactional
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }
    @Override
    @Transactional
    public List<Service> findAllServices() {
        return serviceRepository.findAllServices();
    }
    @Override
    @Transactional
    public List<Service> findAllSubServices() {
        return serviceRepository.findAllSubServices();
    }
    @Override
    @Transactional
    public Optional<Service> findByName(String name) {
        return serviceRepository.findServiceByName(name);
    }
}
