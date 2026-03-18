package com.example.reserves.repository;

import com.example.reserves.model.LocationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface LocationScheduleRepository extends JpaRepository<LocationSchedule, Long> {
    Optional<LocationSchedule> findByLocationIdAndDayOfWeek(Long locationId, DayOfWeek dayOfWeek);
}