package com.example.reserves.controller;

import com.example.reserves.dto.response.LocationResponse;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Location;
import com.example.reserves.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponse>> findAll() {
        List<LocationResponse> locations = locationService.findAll().stream()
                .map(l -> new LocationResponse(
                        l.getId(),
                        l.getLocationName(),
                        l.getAddress(),
                        l.getPhone(),
                        l.getEmail()
                )).toList();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> findById(@PathVariable Long id)
            throws InstanceNotFoundException {
        Location l = locationService.findById(id);
        return ResponseEntity.ok(new LocationResponse(
                l.getId(), l.getLocationName(), l.getAddress(), l.getPhone(), l.getEmail()
        ));
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<LocationResponse> create(@RequestBody Location location) {
        Location saved = locationService.create(location);
        return ResponseEntity.status(201).body(new LocationResponse(
                saved.getId(), saved.getLocationName(), saved.getAddress(),
                saved.getPhone(), saved.getEmail()
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<LocationResponse> update(@PathVariable Long id,
                                                   @RequestBody Location location)
            throws InstanceNotFoundException {
        Location updated = locationService.update(id, location);
        return ResponseEntity.ok(new LocationResponse(
                updated.getId(), updated.getLocationName(), updated.getAddress(),
                updated.getPhone(), updated.getEmail()
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id)
            throws InstanceNotFoundException {
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}