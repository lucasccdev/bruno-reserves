package com.example.reserves.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String serviceName;
    private String description;
    private Integer duration;
    private BigDecimal price;
    private Long locationId;
}