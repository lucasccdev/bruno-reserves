package com.example.reserves.controller;

import com.example.reserves.dto.response.BarberResponse;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Barber;
import com.example.reserves.service.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/barbers")
public class BarberController {

    @Autowired
    private BarberService barberService;

    @GetMapping
    public ResponseEntity<List<BarberResponse>> findAll() {
        return ResponseEntity.ok(toResponseList(barberService.findAll()));
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<BarberResponse>> findByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(toResponseList(barberService.findByLocation(locationId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberResponse> findById(@PathVariable Long id)
            throws InstanceNotFoundException {
        return ResponseEntity.ok(toResponse(barberService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<BarberResponse> create(@RequestBody Barber barber) {
        return ResponseEntity.status(201).body(toResponse(barberService.create(barber)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<BarberResponse> update(@PathVariable Long id,
                                                 @RequestBody Barber barber)
            throws InstanceNotFoundException {
        return ResponseEntity.ok(toResponse(barberService.update(id, barber)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id)
            throws InstanceNotFoundException {
        barberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BarberResponse toResponse(Barber b) {
        return new BarberResponse(
                b.getId(),
                b.getBarberName(),
                b.getPhone(),
                b.getPhoto(),
                b.getLocations().stream()
                        .map(l -> l.getId())
                        .collect(Collectors.toSet())
        );
    }

    private List<BarberResponse> toResponseList(List<Barber> barbers) {
        return barbers.stream().map(this::toResponse).toList();
    }
}