package com.example.reserves.controller;

import com.example.reserves.dto.response.ServiceResponse;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Service;
import com.example.reserves.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ServiceResponse>> findByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(toResponseList(serviceService.findByLocation(locationId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> findById(@PathVariable Long id)
            throws InstanceNotFoundException {
        return ResponseEntity.ok(toResponse(serviceService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<ServiceResponse> create(@RequestBody Service service) {
        return ResponseEntity.status(201).body(toResponse(serviceService.create(service)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<ServiceResponse> update(@PathVariable Long id,
                                                  @RequestBody Service service)
            throws InstanceNotFoundException {
        return ResponseEntity.ok(toResponse(serviceService.update(id, service)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id)
            throws InstanceNotFoundException {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ServiceResponse toResponse(Service s) {
        return new ServiceResponse(
                s.getId(),
                s.getServiceName(),
                s.getDescription(),
                s.getDuration(),
                s.getPrice(),
                s.getLocation().getId()
        );
    }

    private List<ServiceResponse> toResponseList(List<Service> services) {
        return services.stream().map(this::toResponse).toList();
    }
}