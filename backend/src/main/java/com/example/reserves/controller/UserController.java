package com.example.reserves.controller;

import com.example.reserves.dto.request.LoginRequestDto;
import com.example.reserves.dto.response.LoginResponseDto;
import com.example.reserves.exceptions.IncorrectLoginException;
import com.example.reserves.model.User;
import com.example.reserves.security.jwt.JwtUtils;
import com.example.reserves.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());

            String token = jwtUtils.generateToken(user);

            return ResponseEntity.ok(new LoginResponseDto(
                    token,
                    user.getName(),
                    user.getUserEmail(),
                    user.getUserRole().name()
            ));
        } catch (IncorrectLoginException e) {
            return ResponseEntity.status(401).body("Email o contraseña incorrectos");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }
}
