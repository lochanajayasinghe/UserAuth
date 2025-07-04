package com.example.Login.controller;

import com.example.Login.model.User;
import com.example.Login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user,
                           HttpServletRequest request,
                           Model model) {
        String result = authService.register(user, request);
        if ("success".equals(result)) {
            model.addAttribute("message", "Check your email to verify your account.");
            return "login";
        } else {
            model.addAttribute("error", result);
            return "register";
        }
    }

    // Show login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Email verification
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String result = authService.verifyEmail(token);
        if (result.equals("success")) {
            model.addAttribute("message", "Email verified! You can login now.");
            return "login";
        } else {
            model.addAttribute("error", result);
            return "error";
        }
    }

    // Show forgot password page
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    // Handle forgot password form
    @PostMapping("/forgot-password")
    public String sendResetLink(@RequestParam("email") String email, HttpServletRequest request, Model model) {
        String result = authService.sendPasswordResetLink(email, request);
        if (result.equals("success")) {
            model.addAttribute("message", "Reset link sent to your email.");
        } else {
            model.addAttribute("error", result);
        }
        return "forgot-password";
    }

    // Show reset password form
    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    // Handle reset password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token,
                                @RequestParam("password") String password,
                                Model model) {
        String result = authService.resetPassword(token, password);
        if (result.equals("success")) {
            model.addAttribute("message", "Password reset successfully. You can login.");
            return "login";
        } else {
            model.addAttribute("error", result);
            return "reset-password";
        }
    }

    // Basic user home (secured)
    @GetMapping("/user/home")
    public String userHome() {
        return "user-home";
    }

    // Basic admin home (secured)
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin-home";
    }
}
