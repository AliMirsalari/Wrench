package com.ali.mirsalari.wrench.service.impl;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.exception.NotFoundException;
import com.ali.mirsalari.wrench.exception.ServiceAlreadyExistsException;
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
    public Service saveSubservice(String name, Long basePrice, String description, Long serviceParentId) {
        Service parentService = findById(serviceParentId).orElse(null);
        Service service = new Service(name, basePrice, description, parentService);
        if (findByName(service.getName()).isPresent()) {
            throw new ServiceAlreadyExistsException("Service " + service.getName() + " already exists");
        }
        return serviceRepository.save(service);

    }
    @Override
    public Service saveServices(String name){
        Service service = new Service(name);
        return serviceRepository.save(service);
    }

    @Override
    public Service updateSubservice(Long id, String name, Long basePrice, String description, Long serviceParentId) {
        Service service = findById(id).orElseThrow(() -> new NotFoundException("Service is not found!"));
        Service parentService = findById(serviceParentId).orElse(null);
        service.setName(name);
        service.setBasePrice(basePrice);
        service.setDescription(description);
        service.setServiceParent(parentService);
        return serviceRepository.save(service);
    }
    @Override
    public Service updateService(Long id, String name) {
        Service service = findById(id).orElseThrow(() -> new NotFoundException("Service is not found!"));
        service.setName(name);
        return serviceRepository.save(service);
    }

    @Override
    public Service updateWithEntity(Service service) {
        return serviceRepository.save(service);
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
    public List<Service> findAllSubservices() {
        return serviceRepository.findAllSubservices();
    }

    @Override
    public Optional<Service> findByName(String name) {
        return serviceRepository.findServiceByName(name);
    }
}
