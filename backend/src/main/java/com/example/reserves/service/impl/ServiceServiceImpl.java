package com.example.reserves.service.impl;

import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Service;
import com.example.reserves.repository.ServiceRepository;
import com.example.reserves.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<Service> findByLocation(Long locationId) {
        return serviceRepository.findByLocationIdAndActiveTrue(locationId);
    }

    @Override
    public Service findById(Long id) throws InstanceNotFoundException {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Service", id));
    }

    @Override
    public Service create(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public Service update(Long id, Service service) throws InstanceNotFoundException {
        Service existing = serviceRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Service", id));

        existing.setServiceName(service.getServiceName());
        existing.setDescription(service.getDescription());
        existing.setDuration(service.getDuration());
        existing.setPrice(service.getPrice());

        return serviceRepository.save(existing);
    }

    @Override
    public void delete(Long id) throws InstanceNotFoundException {
        Service existing = serviceRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Service", id));

        existing.setActive(false);
        serviceRepository.save(existing);
    }
}