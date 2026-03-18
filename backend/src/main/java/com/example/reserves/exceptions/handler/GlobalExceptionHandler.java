package com.example.reserves.exceptions.handler;

import com.example.reserves.exceptions.AlreadyReservedException;
import com.example.reserves.exceptions.IncorrectLoginException;
import com.example.reserves.exceptions.InstanceNotFoundException;
import com.example.reserves.exceptions.PermissionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncorrectLoginException.class)
    public ResponseEntity<String> handleIncorrectLogin(IncorrectLoginException e) {
        return ResponseEntity.status(401).body("Email o contraseña incorrectos");
    }

    @ExceptionHandler(InstanceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(InstanceNotFoundException e) {
        return ResponseEntity.status(404).body(e.getName() + " no encontrado: " + e.getKey());
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<String> handlePermission(PermissionException e) {
        return ResponseEntity.status(403).body("No tienes permisos para realizar esta acción");
    }

    @ExceptionHandler(AlreadyReservedException.class)
    public ResponseEntity<String> handleAlreadyReserved(AlreadyReservedException e) {
        return ResponseEntity.status(409).body("Esta franja horaria ya está reservada");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception e) {
        return ResponseEntity.status(500).body("Error interno del servidor");
    }
}