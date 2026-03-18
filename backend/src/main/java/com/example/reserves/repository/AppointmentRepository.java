package com.example.reserves.repository;

import com.example.reserves.model.Appointment;
import com.example.reserves.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Reservas de un barbero en un rango de tiempo (para calcular disponibilidad)
    @Query("SELECT a FROM Appointment a WHERE a.barber.id = :barberId " +
            "AND a.status != 'CANCELLED' " +
            "AND a.startTime < :end AND a.endTime > :start")
    List<Appointment> findByBarberAndTimeRange(
            @Param("barberId") Long barberId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Reservas de un local por día
    @Query("SELECT a FROM Appointment a WHERE a.location.id = :locationId " +
            "AND DATE(a.startTime) = DATE(:date) " +
            "AND a.status != 'CANCELLED'")
    List<Appointment> findByLocationAndDate(
            @Param("locationId") Long locationId,
            @Param("date") LocalDateTime date);

    // Reservas de un barbero por día
    @Query("SELECT a FROM Appointment a WHERE a.barber.id = :barberId " +
            "AND DATE(a.startTime) = DATE(:date) " +
            "AND a.status != 'CANCELLED'")
    List<Appointment> findByBarberAndDate(
            @Param("barberId") Long barberId,
            @Param("date") LocalDateTime date);

    // Recordatorios pendientes de enviar
    @Query("SELECT a FROM Appointment a WHERE a.status = 'PENDING' " +
            "AND a.reminderSent = false " +
            "AND a.startTime BETWEEN :from AND :to")
    List<Appointment> findPendingReminders(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("SELECT a FROM Appointment a WHERE a.barber.id = :barberId " +
            "AND a.status = com.example.reserves.model.AppointmentStatus.PENDING " +
            "AND a.startTime >= :now " +
            "ORDER BY a.startTime ASC")
    List<Appointment> findUpcomingByBarber(
            @Param("barberId") Long barberId,
            @Param("now") LocalDateTime now);
}