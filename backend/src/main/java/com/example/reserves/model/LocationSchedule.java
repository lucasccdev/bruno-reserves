package com.example.reserves.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "location_schedules")
public class LocationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    @Column(name = "open_time_1")
    private LocalTime openTime1;

    @Column(name = "close_time_1")
    private LocalTime closeTime1;

    @Column(name = "open_time_2")
    private LocalTime openTime2;

    @Column(name = "close_time_2")
    private LocalTime closeTime2;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen = true;
}