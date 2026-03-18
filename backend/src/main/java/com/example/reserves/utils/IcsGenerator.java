package com.example.reserves.utils;

import com.example.reserves.model.Appointment;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class IcsGenerator {

    private static final DateTimeFormatter ICS_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    public String generate(Appointment a) {
        String start = a.getStartTime().format(ICS_FORMAT);
        String end = a.getEndTime().format(ICS_FORMAT);
        String uid = "appointment-" + a.getId() + "@barberia.com";

        return """
                BEGIN:VCALENDAR
                VERSION:2.0
                PRODID:-//Barberia//Reservas//ES
                BEGIN:VEVENT
                UID:%s
                DTSTAMP:%s
                DTSTART:%s
                DTEND:%s
                SUMMARY:Cita en %s
                DESCRIPTION:Servicio: %s\\nBarbero: %s
                LOCATION:%s
                STATUS:CONFIRMED
                END:VEVENT
                END:VCALENDAR
                """.formatted(
                uid,
                start,
                start,
                end,
                a.getLocation().getLocationName(),
                a.getService().getServiceName(),
                a.getBarber().getBarberName(),
                a.getLocation().getAddress()
        );
    }
}