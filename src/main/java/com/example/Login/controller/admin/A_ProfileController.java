
package com.example.Login.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.Login.repository.UserRepository;
import com.example.Login.model.User;

@Controller

public class A_ProfileController {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/profile")
    public String showProfile(Model model, Authentication authentication) {
        // DEBUG: Print all users to the console and their usernames for case/whitespace issues
        System.out.println("--- ALL USERS IN DATABASE ---");
        for (User u : userRepository.findAll()) {
            System.out.println("username: [" + u.getUsername() + "] enabled: " + u.isEnabled() + " email: " + u.getEmail() + " fullName: " + u.getFullName());
        }
        // Use the exact username from the database, trimmed and lowercased for safety
        String username = "sashinlochana";
        username = username.trim();
        User user = null;
        for (User u : userRepository.findAll()) {
            if (u.getUsername() != null && u.getUsername().trim().equalsIgnoreCase(username)) {
                user = u;
                break;
            }
        }
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("fullName", user.getFullName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("description", user.getDescription());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("roles", user.getRoles());
        } else {
            model.addAttribute("username", "not found");
            model.addAttribute("fullName", "not found");
            model.addAttribute("email", "not found");
            model.addAttribute("description", "not found");
            model.addAttribute("phone", "not found");
            model.addAttribute("roles", null);
        }
        return "admin/profile";
    }
}
