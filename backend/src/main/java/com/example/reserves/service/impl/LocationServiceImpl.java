package com.example.reserves.service.impl;

import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Location;
import com.example.reserves.repository.LocationRepository;
import com.example.reserves.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public List<Location> findAll() {
        return locationRepository.findByActiveTrue();
    }

    @Override
    public Location findById(Long id) throws InstanceNotFoundException {
        return locationRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Location", id));
    }

    @Override
    public Location create(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location update(Long id, Location location) throws InstanceNotFoundException {
        Location existing = locationRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Location", id));

        existing.setLocationName(location.getLocationName());
        existing.setAddress(location.getAddress());
        existing.setPhone(location.getPhone());
        existing.setEmail(location.getEmail());

        return locationRepository.save(existing);
    }

    @Override
    public void delete(Long id) throws InstanceNotFoundException {
        Location existing = locationRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Location", id));

        existing.setActive(false);
        locationRepository.save(existing);
    }
}