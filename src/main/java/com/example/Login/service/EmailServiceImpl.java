package com.example.Login.service;

import com.example.Login.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendVerificationEmail(User user, String verificationUrl) {
        String subject = "Verify Your Email Address";
        String body = String.format(
            "Dear %s,\n\n" +
            "Thank you for registering. Please click the link below to verify your email address:\n\n" +
            "%s\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "Best regards,\n" +
            "The Support Team",
            user.getFullName(),
            verificationUrl
        );
        
        sendEmail(user.getEmail(), subject, body);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetLink) {
        String subject = "Password Reset Request";
        String body = String.format(
            "We received a request to reset your password. Click the link below to proceed:\n\n" +
            "%s\n\n" +
            "If you didn't request this, please ignore this email.\n\n" +
            "This link will expire in 1 hour.\n\n" +
            "Best regards,\n" +
            "The Support Team",
            resetLink
        );
        
        sendEmail(email, subject, body);
    }
}