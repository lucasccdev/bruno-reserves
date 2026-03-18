package com.example.reserves.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class BarberResponse {
    private Long id;
    private String barberName;
    private String phone;
    private String photo;
    private Set<Long> locationIds;
}