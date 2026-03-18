package com.example.reserves.service;

import com.example.reserves.exceptions.IncorrectLoginException;
import com.example.reserves.model.User;

public interface UserService {
    User login(String email, String password) throws IncorrectLoginException;
}
