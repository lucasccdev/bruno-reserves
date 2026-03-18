package com.example.reserves.service;

import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Service;

import java.util.List;

public interface ServiceService {
    List<Service> findByLocation(Long locationId);
    Service findById(Long id) throws InstanceNotFoundException;
    Service create(Service service);
    Service update(Long id, Service service) throws InstanceNotFoundException;
    void delete(Long id) throws InstanceNotFoundException;
}