package com.example.Login.service;

import com.example.Login.model.PasswordResetToken;
import com.example.Login.model.User;
import com.example.Login.repository.PasswordResetTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public String register(User user, HttpServletRequest request) {
        return userService.registerUser(user, request);
    }

    public String verifyEmail(String token) {
        return userService.verifyUser(token);
    }

    public String sendPasswordResetLink(String email, HttpServletRequest request) {
        return userService.createPasswordResetToken(email, request);
    }

    public String resetPassword(String token, String newPassword) {
        return userService.resetPassword(token, newPassword);
    }

    public void validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }
    }
}

