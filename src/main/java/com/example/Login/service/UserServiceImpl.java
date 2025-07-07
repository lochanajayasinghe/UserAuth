package com.example.Login.service;

import com.example.Login.model.*;
import com.example.Login.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
    public String registerUser(User user, HttpServletRequest request) {
        // Check if email is already registered
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);

        // Get user role (safely)
        Optional<Role> userRoleOpt = roleRepository.findByName("ROLE_USER");
        if (userRoleOpt.isEmpty()) {
            return "User role not found in database!";
        }

        user.getRoles().add(userRoleOpt.get());
        userRepository.save(user);

        // Generate verification token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();

        verificationTokenRepository.save(verificationToken);

        // Construct verification link
        String verificationLink = request.getRequestURL().toString().replace("/register", "") +
                "/verify?token=" + token;

        // Send email
        emailService.sendEmail(user.getEmail(), "Verify your account", "Click the link: " + verificationLink);

        return "success";
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

        // Delete any existing tokens for this user first
        passwordResetTokenRepository.deleteByUser(user);

        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();

        passwordResetTokenRepository.save(resetToken);

        // Construct reset link
        String resetLink = request.getRequestURL().toString()
                .replace(request.getRequestURI(), "")
                + "/reset-password?token=" + token;

        // Send email with proper HTML template
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

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

        // Delete the used token
        passwordResetTokenRepository.delete(resetToken);

        return "success";
    }

    @Override
    public void validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        PasswordResetToken resetToken = optionalToken.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }
    }
}

