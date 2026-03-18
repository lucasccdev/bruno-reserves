package com.example.reserves.service.impl;

import com.example.reserves.dto.request.AppointmentRequest;
import com.example.reserves.dto.response.AvailableSlotResponse;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.model.*;
import com.example.reserves.repository.*;
import com.example.reserves.service.AppointmentService;
import com.example.reserves.service.EmailService;
import com.example.reserves.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private LocationScheduleRepository scheduleRepository;

    @Autowired
    private BarberRepository barberRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public List<AvailableSlotResponse> getAvailableSlots(Long locationId, Long serviceId,
                                                         Long barberId, LocalDate date)
            throws InstanceNotFoundException {

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new InstanceNotFoundException("Service", serviceId));

        LocationSchedule schedule = scheduleRepository
                .findByLocationIdAndDayOfWeek(locationId, date.getDayOfWeek())
                .orElseThrow(() -> new InstanceNotFoundException("Schedule", locationId));

        if (!schedule.isOpen()) return List.of();

        // Barberos a considerar
        List<Barber> barbers;
        if (barberId != null) {
            Barber barber = barberRepository.findById(barberId)
                    .orElseThrow(() -> new InstanceNotFoundException("Barber", barberId));
            barbers = List.of(barber);
        } else {
            barbers = barberRepository.findByLocationIdAndActiveTrue(locationId);
        }

        int duration = service.getDuration();
        List<AvailableSlotResponse> slots = new ArrayList<>();

        for (Barber barber : barbers) {
            // Huecos del turno de mañana
            if (schedule.getOpenTime1() != null) {
                slots.addAll(getSlotsForRange(
                        date, schedule.getOpenTime1(), schedule.getCloseTime1(),
                        duration, barber));
            }
            // Huecos del turno de tarde
            if (schedule.getOpenTime2() != null) {
                slots.addAll(getSlotsForRange(
                        date, schedule.getOpenTime2(), schedule.getCloseTime2(),
                        duration, barber));
            }
        }

        return slots;
    }

    private List<AvailableSlotResponse> getSlotsForRange(LocalDate date, LocalTime open,
                                                         LocalTime close, int duration,
                                                         Barber barber) {
        List<AvailableSlotResponse> slots = new ArrayList<>();
        LocalDateTime current = LocalDateTime.of(date, open);
        LocalDateTime rangeEnd = LocalDateTime.of(date, close);

        while (!current.plusMinutes(duration).isAfter(rangeEnd)) {
            LocalDateTime slotEnd = current.plusMinutes(duration);

            // Verificar si el barbero está libre en este hueco
            List<Appointment> conflicts = appointmentRepository
                    .findByBarberAndTimeRange(barber.getId(), current, slotEnd);

            if (conflicts.isEmpty()) {
                slots.add(new AvailableSlotResponse(
                        current, slotEnd, barber.getId(), barber.getBarberName()));
            }

            current = current.plusMinutes(duration);
        }

        return slots;
    }

    @Override
    public Appointment create(AppointmentRequest request) throws InstanceNotFoundException {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new InstanceNotFoundException("Location", request.getLocationId()));

        com.example.reserves.model.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new InstanceNotFoundException("Service", request.getServiceId()));

        // ===== VALIDACIÓN 1: el startTime está dentro del horario del local =====
        LocalDate date = request.getStartTime().toLocalDate();
        LocalTime time = request.getStartTime().toLocalTime();

        LocationSchedule schedule = scheduleRepository
                .findByLocationIdAndDayOfWeek(location.getId(), date.getDayOfWeek())
                .orElseThrow(() -> new InstanceNotFoundException("Schedule", location.getId()));

        if (!schedule.isOpen()) {
            throw new IllegalArgumentException("El local está cerrado ese día");
        }

        boolean inMorning = schedule.getOpenTime1() != null &&
                !time.isBefore(schedule.getOpenTime1()) &&
                time.plusMinutes(service.getDuration()).compareTo(schedule.getCloseTime1()) <= 0;

        boolean inAfternoon = schedule.getOpenTime2() != null &&
                !time.isBefore(schedule.getOpenTime2()) &&
                time.plusMinutes(service.getDuration()).compareTo(schedule.getCloseTime2()) <= 0;

        if (!inMorning && !inAfternoon) {
            throw new IllegalArgumentException(
                    "La hora seleccionada está fuera del horario del local");
        }

        // ===== VALIDACIÓN 2: el barbero pertenece al local =====
        Barber barber;
        if (request.getBarberId() != null) {
            barber = barberRepository.findById(request.getBarberId())
                    .orElseThrow(() -> new InstanceNotFoundException("Barber", request.getBarberId()));

            boolean barberBelongsToLocation = barber.getLocations().stream()
                    .anyMatch(l -> l.getId().equals(location.getId()));

            if (!barberBelongsToLocation) {
                throw new IllegalArgumentException(
                        "El barbero no pertenece a este local");
            }
        } else {
            List<Barber> available = barberRepository.findByLocationIdAndActiveTrue(request.getLocationId());
            barber = available.stream()
                    .filter(b -> appointmentRepository.findByBarberAndTimeRange(
                            b.getId(),
                            request.getStartTime(),
                            request.getStartTime().plusMinutes(service.getDuration())
                    ).isEmpty())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No hay barberos disponibles en ese horario"));
        }

        Appointment appointment = new Appointment();
        appointment.setLocation(location);
        appointment.setBarber(barber);
        appointment.setService(service);
        appointment.setClientName(request.getClientName());
        appointment.setClientPhone(request.getClientPhone());
        appointment.setClientEmail(request.getClientEmail());
        appointment.setNotes(request.getNotes());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getStartTime().plusMinutes(service.getDuration()));
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);
        emailService.sendConfirmation(saved);
        return saved;
    }

    @Override
    public Appointment complete(Long id) throws InstanceNotFoundException {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Appointment", id));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancel(Long id) throws InstanceNotFoundException {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Appointment", id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> findByLocationAndDate(Long locationId, LocalDateTime date)
            throws InstanceNotFoundException {
        return appointmentRepository.findByLocationAndDate(locationId, date);
    }

    @Override
    public Appointment findById(Long id) throws InstanceNotFoundException {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException("Appointment", id));
    }

    @Override
    public List<Appointment> findMyAppointments(HttpServletRequest request)
            throws InstanceNotFoundException {

        User user = securityUtils.getCurrentUser(request);

        if (user.getBarber() == null) {
            throw new InstanceNotFoundException("Barber vinculado al usuario", user.getId());
        }

        return appointmentRepository.findUpcomingByBarber(
                user.getBarber().getId(),
                LocalDateTime.now()
        );
    }
}