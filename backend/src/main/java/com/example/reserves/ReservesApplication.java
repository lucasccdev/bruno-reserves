package com.example.reserves;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReservesApplication.class, args);
    }
}