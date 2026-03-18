package com.example.reserves.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationResponse {
    private Long id;
    private String locationName;
    private String address;
    private String phone;
    private String email;
}