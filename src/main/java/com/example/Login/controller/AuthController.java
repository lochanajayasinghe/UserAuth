package com.example.Login.controller;

import com.example.Login.model.User;
import com.example.Login.repository.RoleRepository;
import com.example.Login.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RoleRepository roleRepository;

    @GetMapping("/")
    public String landing_page(Model model) {
        return "home/landing_page";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home/home";
    }

    @GetMapping("/about")
    public String aboutUs(Model model) {
        return "home/about";
    }

    @GetMapping("/contactUs")
    public String contactUs(Model model) {
        return "home/contactUs";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/check-username")
    @ResponseBody
    public Map<String, Boolean> checkUsernameAvailability(@RequestParam String username) {
        boolean available = !authService.existsByUsername(username);
        return Collections.singletonMap("available", available);
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               HttpServletRequest request,
                               Model model) {
        String result = authService.registerUser(user, request);
        if ("success".equals(result)) {
            model.addAttribute("message", "Registration successful! Please check your email to verify your account.");
        } else {
            model.addAttribute("error", result);
        }
        return "register";
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token, Model model) {
        String result = authService.verifyUser(token);
        if ("success".equals(result)) {
            model.addAttribute("message", "Email verified. You can now log in.");
        } else {
            model.addAttribute("error", result);
        }
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam String email,
            HttpServletRequest request,
            Model model) {
        try {
            String result = authService.createPasswordResetToken(email, request);
            if ("success".equals(result)) {
                model.addAttribute("message", "Password reset link sent to your email");
            } else {
                model.addAttribute("error", result);
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        try {
            authService.validatePasswordResetToken(token);
            model.addAttribute("token", token);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                 @RequestParam String password,
                                 Model model) {
        try {
            String result = authService.resetPassword(token, password);
            if ("success".equals(result)) {
                model.addAttribute("message", "Password updated successfully. You can now login.");
                return "login";
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", token);
        }
        return "reset-password";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "user-home";
    }

    @GetMapping("/Asset")
    public String Asset(Model model) {
        return "Asset/Asset";
    }
    @GetMapping("/AssetHistory")
    public String AssetHistory(Model model) {
        return "AssetHistory/AssetHistory";
    }
    @GetMapping("/Condemn")
    public String Condemn(Model model) {
        return "Condemn/Condemn";
    }
    @GetMapping("/Dashboard")
    public String Dashboard(Model model) {
        return "Dashboard/Dashboard";
    }
    @GetMapping("/Invoice")
    public String Invoice(Model model) {
        return "Invoice/Invoice";
    }
    @GetMapping("/Movement")
    public String Movement(Model model) {
        return "Movement/Movement";
    }
    @GetMapping("/UserHistory")
    public String UserHistory(Model model) {
        return "UserHistory/UserHistory";
    }
    @GetMapping("/Maintain")
    public String Maintain(Model model) {
        return "Maintain/Maintain";
    }

}