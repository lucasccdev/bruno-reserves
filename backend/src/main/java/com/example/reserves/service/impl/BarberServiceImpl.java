package com.example.reserves.service.impl;

import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Barber;
import com.example.reserves.repository.BarberRepository;
import com.example.reserves.service.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberServiceImpl implements BarberService {

    @Autowired
    private BarberRepository barberRepository;

    @Override
    public List<Barber> findAll() {
        return barberRepository.findByActiveTrue();
    }

    @Override
    public List<Barber> findByLocation(Long locationId) {
        return barberRepository.findByLocationIdAndActiveTrue(locationId);
    }

    @Override
    public Barber findById(Long id) throws InstanceNotFoundException {
        return barberRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Barber", id));
    }

    @Override
    public Barber create(Barber barber) {
        return barberRepository.save(barber);
    }

    @Override
    public Barber update(Long id, Barber barber) throws InstanceNotFoundException {
        Barber existing = barberRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Barber", id));

        existing.setBarberName(barber.getBarberName());
        existing.setPhone(barber.getPhone());
        existing.setPhoto(barber.getPhoto());
        existing.setLocations(barber.getLocations());

        return barberRepository.save(existing);
    }

    @Override
    public void delete(Long id) throws InstanceNotFoundException {
        Barber existing = barberRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Barber", id));

        existing.setActive(false);
        barberRepository.save(existing);
    }
}