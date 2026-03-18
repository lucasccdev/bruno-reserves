package com.example.reserves.service;

import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();
    Location findById(Long id) throws InstanceNotFoundException;
    Location create(Location location);
    Location update(Long id, Location location) throws InstanceNotFoundException;
    void delete(Long id) throws InstanceNotFoundException;
}