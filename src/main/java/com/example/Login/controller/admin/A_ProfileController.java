
package com.example.Login.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class A_ProfileController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/profile")
    public String showProfile(Model model, Authentication authentication) {
        // Optionally, add user details to the model if needed
        model.addAttribute("username", authentication.getName());
        return "admin/profile";
    }
}
