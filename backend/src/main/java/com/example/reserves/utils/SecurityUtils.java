package com.example.reserves.utils;

import com.example.reserves.model.User;
import com.example.reserves.repository.UserRepository;
import com.example.reserves.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtUtils.extractUserId(token);
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isSuperAdmin(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && user.getUserRole().name().equals("SUPERADMIN");
    }

    public boolean canAccessLocation(HttpServletRequest request, Long locationId) {
        User user = getCurrentUser(request);
        if (user == null) return false;
        if (user.getUserRole().name().equals("SUPERADMIN")) return true;
        return user.getLocation() != null &&
                user.getLocation().getId().equals(locationId);
    }
}