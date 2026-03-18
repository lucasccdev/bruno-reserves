package com.example.reserves.repository;

import com.example.reserves.model.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long> {
    List<Barber> findByActiveTrue();
    @Query("SELECT b FROM Barber b JOIN b.locations l WHERE l.id = :locationId AND b.active = true")
    List<Barber> findByLocationIdAndActiveTrue(@Param("locationId") Long locationId);
}