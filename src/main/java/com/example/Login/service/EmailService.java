package com.example.Login.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void sendPasswordResetEmail(String email, String resetLink);
}