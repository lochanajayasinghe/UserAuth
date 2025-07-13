package com.example.Login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize; // Import for method-level security

@Controller
@RequestMapping
public class AdminDirectorController {

    // Admin Dashboard - Accessible only by ADMIN role
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin-home"; // Maps to src/main/resources/templates/admin-home.html
    }

    // Director Dashboard - Accessible only by DIRECTOR role
    @PreAuthorize("hasRole('DIRECTOR')")
    @GetMapping("/director/home")
    public String directorHome() {
        return "director-home"; // Maps to src/main/resources/templates/director-home.html
    }

    // Access Denied Page - Accessible by anyone (even unauthenticated)
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Maps to src/main/resources/templates/access-denied.html
    }

    // You can add more role-specific endpoints here, for example:
    // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping("/admin/manage-users")
    // public String manageUsers() {
    //     return "admin/manage-users";
    // }

    // @PreAuthorize("hasRole('DIRECTOR')")
    // @GetMapping("/director/reports")
    // public String directorReports() {
    //     return "director/reports";
    // }
}
