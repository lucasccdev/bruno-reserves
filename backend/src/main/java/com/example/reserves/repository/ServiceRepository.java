package com.example.reserves.repository;

import com.example.reserves.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByLocationIdAndActiveTrue(Long locationId);
}