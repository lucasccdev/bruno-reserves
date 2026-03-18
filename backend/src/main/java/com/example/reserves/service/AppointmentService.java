package com.example.reserves.service;

import com.example.reserves.dto.request.AppointmentRequest;
import com.example.reserves.dto.response.AvailableSlotResponse;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.Appointment;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    List<AvailableSlotResponse> getAvailableSlots(Long locationId, Long serviceId, Long barberId, LocalDate date) throws InstanceNotFoundException;
    Appointment create(AppointmentRequest request) throws InstanceNotFoundException;
    Appointment complete(Long id) throws InstanceNotFoundException;
    Appointment cancel(Long id) throws InstanceNotFoundException;
    List<Appointment> findByLocationAndDate(Long locationId, LocalDateTime date) throws InstanceNotFoundException;
    Appointment findById(Long id) throws InstanceNotFoundException;
    List<Appointment> findMyAppointments(HttpServletRequest request) throws InstanceNotFoundException;
}