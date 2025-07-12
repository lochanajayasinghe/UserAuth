package com.example.Login.service;

import com.example.Login.model.*;
import com.example.Login.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public String registerUser(User user, HttpServletRequest request) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                return "Email already registered!";
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                return "Username already taken!";
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(false);

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            user.setRoles(Collections.singleton(userRole));

            userRepository.save(user);

            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = VerificationToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(24))
                    .build();
            verificationTokenRepository.save(verificationToken);

            String verificationUrl = String.format("%s/verify?token=%s",
                    request.getRequestURL().toString().replace(request.getRequestURI(), ""),
                    token);

            emailService.sendVerificationEmail(user, verificationUrl);
            return "success";
        } catch (Exception e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    @Override
    @Transactional
    public String verifyUser(String token) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) return "Invalid token!";

        VerificationToken verificationToken = optionalToken.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token expired!";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        return "success";
    }

    @Override
    @Transactional
    public String createPasswordResetToken(String email, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) return "Email not found!";

        User user = optionalUser.get();
        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);

        String resetLink = request.getRequestURL().toString()
                .replace(request.getRequestURI(), "")
                + "/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(email, resetLink);

        return "success";
    }

    @Override
    @Transactional
    public String resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) return "Invalid token!";

        PasswordResetToken resetToken = optionalToken.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token expired!";
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);

        return "success";
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }
        if (optionalToken.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
    }
}