package com.example.Login.service;

import com.example.Login.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    String registerUser(User user, HttpServletRequest request);
    String verifyUser(String token);
    String createPasswordResetToken(String email, HttpServletRequest request);
    String resetPassword(String token, String newPassword);
    boolean existsByUsername(String username);
    void validatePasswordResetToken(String token);
}