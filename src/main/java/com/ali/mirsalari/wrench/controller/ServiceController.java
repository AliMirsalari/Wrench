package com.ali.mirsalari.wrench.controller;

import com.ali.mirsalari.wrench.entity.Service;
import com.ali.mirsalari.wrench.service.ServiceService;
import com.ali.mirsalari.wrench.controller.dto.RegisterServiceRequest;
import com.ali.mirsalari.wrench.controller.dto.ServiceResponse;
import com.ali.mirsalari.wrench.controller.mapper.ServiceResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/service")

public class ServiceController {
    private final ServiceResponseMapper serviceResponseMapper;
    private final ServiceService serviceService;

    @PreAuthorize("hasAnyRole('ADMIN','EXPERT','CUSTOMER')")
    @GetMapping({"","/"})
    public List<ServiceResponse> getServices() {
        List<Service> services = serviceService.findAll();
        return services.stream()
                .map(serviceResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/registerSubservice")
    public ResponseEntity<?> registerSubservice(@Valid @RequestBody RegisterServiceRequest request) {
        Service service = serviceService.saveSubservice(
                request.name(),
                request.basePrice(),
                request.description(),
                request.serviceParentId());
        return ResponseEntity.ok(serviceResponseMapper.toDto(service));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/registerService/{serviceName}")
    public ResponseEntity<?> registerService(@PathVariable String serviceName) {
        Service service = serviceService.saveServices(serviceName);
        return ResponseEntity.ok(serviceResponseMapper.toDto(service));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "updateSubservice/{id}")
    public ResponseEntity<?> updateSubservice(
                                       @PathVariable("id") Long id,
                                       @Valid @RequestBody RegisterServiceRequest request) {
        Service service = serviceService.updateSubservice(
                id,
                request.name(),
                request.basePrice(),
                request.description(),
                request.serviceParentId());
        return ResponseEntity.ok(serviceResponseMapper.toDto(service));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "updateService/{id}")
    public ResponseEntity<?> updateService(
                                              @PathVariable("id") Long id,
                                              @RequestParam String name) {
        Service service = serviceService.updateService(
                id,
                name);
        return ResponseEntity.ok(serviceResponseMapper.toDto(service));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable("serviceId") Long serviceId) {
        serviceService.remove(serviceId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findById/{id}")
    public ResponseEntity<ServiceResponse> findById(@PathVariable Long id) {
        Optional<Service> service = serviceService.findById(id);
        return service.map(serviceResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAllServices")
    public List<ServiceResponse> findAllServices() {
        List<Service> services = serviceService.findAllServices();
        return services.stream()
                .map(serviceResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findAllSubservices")
    public List<ServiceResponse> findAllSubservices() {
        List<Service> services = serviceService.findAllSubservices();
        return services.stream()
                .map(serviceResponseMapper::toDto)
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/findByName/{name}")
    public ResponseEntity<ServiceResponse> findByName(@PathVariable String name) {
        Optional<Service> service = serviceService.findByName(name);
        return service.map(serviceResponseMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
