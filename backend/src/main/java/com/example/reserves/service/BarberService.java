package com.example.reserves.service;

import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Barber;

import java.util.List;

public interface BarberService {
    List<Barber> findAll();
    List<Barber> findByLocation(Long locationId);
    Barber findById(Long id) throws InstanceNotFoundException;
    Barber create(Barber barber);
    Barber update(Long id, Barber barber) throws InstanceNotFoundException;
    void delete(Long id) throws InstanceNotFoundException;
}