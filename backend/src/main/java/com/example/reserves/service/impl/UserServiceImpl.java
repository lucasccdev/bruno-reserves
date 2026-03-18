package com.example.reserves.service.impl;

import com.example.reserves.exceptions.IncorrectLoginException;
import com.example.reserves.model.User;
import com.example.reserves.repository.UserRepository;
import com.example.reserves.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String email, String password) throws IncorrectLoginException {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new IncorrectLoginException(email, password));

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new IncorrectLoginException(email, password);
        }

        return user;
    }
}