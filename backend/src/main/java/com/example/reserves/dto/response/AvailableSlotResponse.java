package com.example.reserves.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AvailableSlotResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long barberId;
    private String barberName;
}