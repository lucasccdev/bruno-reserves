package com.example.reserves.service;

import com.example.reserves.model.Appointment;

public interface EmailService {
    void sendConfirmation(Appointment appointment);
    void sendReminder(Appointment appointment);
}