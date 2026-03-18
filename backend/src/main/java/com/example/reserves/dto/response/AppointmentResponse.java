package com.example.reserves.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long locationId;
    private String locationName;
    private Long barberId;
    private String barberName;
    private Long serviceId;
    private String serviceName;
    private Integer duration;
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private String notes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}