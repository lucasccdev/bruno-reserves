package com.example.reserves.controller;

import com.example.reserves.dto.request.AppointmentRequest;
import com.example.reserves.dto.response.AppointmentResponse;
import com.example.reserves.dto.response.AvailableSlotResponse;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.exceptions.PermissionException;
import com.example.reserves.model.Appointment;
import com.example.reserves.model.User;
import com.example.reserves.service.AppointmentService;
import com.example.reserves.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SecurityUtils securityUtils;

    // Público — el cliente consulta huecos disponibles
    @GetMapping("/available")
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlots(
            @RequestParam Long locationId,
            @RequestParam Long serviceId,
            @RequestParam(required = false) Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
            throws InstanceNotFoundException {
        return ResponseEntity.ok(
                appointmentService.getAvailableSlots(locationId, serviceId, barberId, date));
    }

    // Público — el cliente crea una reserva
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @Valid @RequestBody AppointmentRequest request) throws InstanceNotFoundException {
        return ResponseEntity.status(201).body(
                toResponse(appointmentService.create(request)));
    }

    // Panel — ver reservas de un local por día
    @GetMapping("/location/{locationId}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<?> findByLocationAndDate(
            @PathVariable Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            HttpServletRequest request) throws InstanceNotFoundException, PermissionException {

        if (!securityUtils.canAccessLocation(request, locationId)) {
            throw new PermissionException();
        }

        return ResponseEntity.ok(
                appointmentService.findByLocationAndDate(locationId, date)
                        .stream().map(this::toResponse).toList());
    }

    // Panel — cancelar
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    public ResponseEntity<?> cancel(@PathVariable Long id,
                                    HttpServletRequest request)
            throws InstanceNotFoundException, PermissionException {

        Appointment appointment = appointmentService.findById(id);

        if (!securityUtils.canAccessLocation(request, appointment.getLocation().getId())) {
            throw new PermissionException();
        }

        return ResponseEntity.ok(toResponse(appointmentService.cancel(id)));
    }

    // Barbero — ver sus propias citas
    @GetMapping("/my")
    @PreAuthorize("hasRole('BARBER')")
    public ResponseEntity<List<AppointmentResponse>> findMyAppointments(
            HttpServletRequest request) throws InstanceNotFoundException {
        return ResponseEntity.ok(
                appointmentService.findMyAppointments(request)
                        .stream().map(this::toResponse).toList());
    }

    // Barbero — marcar su propia cita como completada
    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'BARBER')")
    public ResponseEntity<?> complete(@PathVariable Long id,
                                      HttpServletRequest request)
            throws InstanceNotFoundException, PermissionException {

        Appointment appointment = appointmentService.findById(id);
        User user = securityUtils.getCurrentUser(request);

        // BARBER solo puede completar sus propias citas
        if (user.getUserRole().name().equals("BARBER")) {
            if (user.getBarber() == null ||
                    !user.getBarber().getId().equals(appointment.getBarber().getId())) {
                throw new PermissionException();
            }
        }

        return ResponseEntity.ok(toResponse(appointmentService.complete(id)));
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getLocation().getId(),
                a.getLocation().getLocationName(),
                a.getBarber().getId(),
                a.getBarber().getBarberName(),
                a.getService().getId(),
                a.getService().getServiceName(),
                a.getService().getDuration(),
                a.getClientName(),
                a.getClientPhone(),
                a.getClientEmail(),
                a.getNotes(),
                a.getStartTime(),
                a.getEndTime(),
                a.getStatus().name()
        );
    }
}