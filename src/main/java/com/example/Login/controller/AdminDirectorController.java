package com.example.Login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize; // Import for method-level security

@Controller
@RequestMapping
public class AdminDirectorController {

    @GetMapping("/user/home")
    public String userHome() {
        return "Dashboard/user-home";
    }

    // Admin Dashboard - Accessible only by ADMIN role
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/home")
    public String adminHome() {
        return "Dashboard/admin-home"; // Maps to src/main/resources/templates/admin-home.html
    }

    // Director Dashboard - Accessible only by DIRECTOR role
    @PreAuthorize("hasRole('DIRECTOR')")
    @GetMapping("/director/home")
    public String directorHome() {
        return "Dashboard/director-home"; // Maps to src/main/resources/templates/director-home.html
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

    @GetMapping("/adminhome")
    public String AdminhomePage(Model model) {
        return "home/admin/home";
    }

    @GetMapping("/directorhome")
    public String DirectorhomePage(Model model) {
        return "home/director/home";
    }

    //admin
    // @GetMapping("/admin/adminAsset")
    // public String AdminAsset(Model model) {
    //     return "Asset/admin/Asset";
    // }
    @GetMapping("/admin/adminAssetHistory")
    public String AdminAssetHistory(Model model) {
        return "AssetHistory/admin/AssetHistory";
    }
    @GetMapping("/admin/adminCondemn")
    public String AdminCondemn(Model model) {
        return "Condemn/admin/Condemn";
    }
    // @GetMapping("/adminInvoice")
    // public String AdminInvoice(Model model) {
    //     return "Invoice/admin/Invoice";
    // }
    @GetMapping("/admin/adminMovement")
    public String AdminMovement(Model model) {
        return "Movement/admin/Movement";
    }
    
    @GetMapping("/admin/adminMaintain")
    public String AdminMaintain(Model model) {
        return "Maintain/admin/Maintain";
    }

    //director
    // @GetMapping("/director/directorAsset")
    // public String DirectorAsset(Model model) {
    //     return "Asset/director/Asset";
    // }
    @GetMapping("/director/directorAssetHistory")
    public String DirectorAssetHistory(Model model) {
        return "AssetHistory/director/AssetHistory";
    }
    @GetMapping("/director/directorCondemn")
    public String DirectorCondemn(Model model) {
        return "Condemn/director/Condemn";
    }
    // @GetMapping("/directorInvoice")
    // public String DirectorInvoice(Model model) {
    //     return "Invoice/director/Invoice";
    // }
    @GetMapping("/director/directorMovement")
    public String DirectorMovement(Model model) {
        return "Movement/director/Movement";
    }

    @GetMapping("/director/directorMaintain")
    public String DirectorMaintain(Model model) {
        return "Maintain/director/Maintain";
    }

    
}
