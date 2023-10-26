package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.ServiceExistException;
import com.ali.mirsalari.wrench.repository.ServiceRepository;
import com.ali.mirsalari.wrench.service.ServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;


    @Override
    public Service save(Service service) throws ServiceExistException {
        if (findByName(service.getName()).isPresent()) {
            throw new ServiceExistException("Service " + service.getName() + " already exists");
        }
        return serviceRepository.save(service);

    }
    @Override
    public Service update(Service service) throws NotFoundException {
        if (service.getId() == null || serviceRepository.findById(service.getId()).isEmpty()) {
            throw new NotFoundException("Service with id: " + service.getId() + " is not found.");
        }
        return serviceRepository.save(service);
    }
    @Override
    public Service uncheckedUpdate(Service service) {
        return  serviceRepository.save(service);
    }
    @Override
    public void remove(Long id) {
        serviceRepository.deleteById(id);
    }
    @Override
    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }
    @Override
    public List<Service> findAllServices() {
        return serviceRepository.findAllServices();
    }
    @Override
    public List<Service> findAllSubServices() {
        return serviceRepository.findAllSubServices();
    }
    @Override
    public Optional<Service> findByName(String name) {
        return serviceRepository.findServiceByName(name);
    }
}
