package com.example.Login.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addGlobalUserAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String name = authentication.getName();

            if (!model.containsAttribute("username")) {
                model.addAttribute("username", name);
            }
            // Use the username as a tooltip fallback if a proper email isn't provided by controllers
            if (!model.containsAttribute("email")) {
                model.addAttribute("email", name);
            }
            // Do not override if controllers already provide a profile photo URL
            if (!model.containsAttribute("profilePhotoUrl")) {
                model.addAttribute("profilePhotoUrl", null);
            }
        }
    }
}
