package com.example.Login.controller;

import com.example.Login.model.User;
import com.example.Login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, HttpServletRequest request, Model model) {
        String result = authService.register(user, request);
        if ("success".equals(result)) {
            model.addAttribute("message", "Check your email to verify your account.");
        } else {
            model.addAttribute("error", result);
        }
        return "register";
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token, Model model) {
        String result = authService.verifyEmail(token);
        if ("success".equals(result)) {
            model.addAttribute("message", "Email verified. You can now log in.");
        } else {
            model.addAttribute("error", result);
        }
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendResetLink(@RequestParam String email, HttpServletRequest request, Model model) {
        String result = authService.sendPasswordResetLink(email, request);
        if ("success".equals(result)) {
            model.addAttribute("message", "Password reset link sent to your email.");
        } else {
            model.addAttribute("error", result);
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, Model model) {
        String result = authService.resetPassword(token, password);
        if ("success".equals(result)) {
            model.addAttribute("message", "Password updated successfully.");
        } else {
            model.addAttribute("error", result);
        }
        return "reset-password";
    }
}
