package com.example.reserves.scheduler;

import com.example.reserves.model.Appointment;
import com.example.reserves.repository.AppointmentRepository;
import com.example.reserves.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmailService emailService;

    // Se ejecuta cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void sendReminders() {
        LocalDateTime from = LocalDateTime.now().plusHours(23);
        LocalDateTime to = LocalDateTime.now().plusHours(25);

        System.out.println("⏰ Scheduler ejecutado: " + LocalDateTime.now());
        System.out.println("⏰ Buscando citas entre " + from + " y " + to);

        List<Appointment> appointments = appointmentRepository
                .findPendingReminders(from, to);

        System.out.println("⏰ Recordatorios encontrados: " + appointments.size());

        for (Appointment appointment : appointments) {
            emailService.sendReminder(appointment);
            appointment.setReminderSent(true);
            appointmentRepository.save(appointment);
            System.out.println("✅ Recordatorio enviado a: " + appointment.getClientEmail());
        }
    }
}