package com.example.reserves.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequest {

    @NotNull
    private Long locationId;

    @NotNull
    private Long serviceId;

    private Long barberId;          // null = cualquiera

    @NotBlank
    private String clientName;

    @NotBlank
    private String clientPhone;

    @NotBlank
    @Email
    private String clientEmail;

    private String notes;

    @NotNull
    private LocalDateTime startTime;
}