package com.example.Login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;

@Controller
@RequestMapping
public class AdminDirectorController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/home")
    public String userHome() {
        return "Dashboard/user-home";
    }

    // Admin Dashboard - Accessible only by ADMIN role
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/home")
    public String adminHome(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("email", user.getEmail());
                // Get first role name (or empty if none)
                String role = user.getRoles().stream().findFirst().map(r -> r.getName().replace("ROLE_", "")).orElse("");
                model.addAttribute("role", role);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("email", "");
                model.addAttribute("role", "");
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("email", "");
            model.addAttribute("role", "");
        }
        return "Dashboard/admin-home";
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
    @GetMapping("/admin/adminAssetAllocation")
    public String AdminAssetAllocation(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("email", user.getEmail());
                String role = user.getRoles().stream().findFirst().map(r -> r.getName().replace("ROLE_", "")).orElse("");
                model.addAttribute("role", role);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("email", "");
                model.addAttribute("role", "");
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("email", "");
            model.addAttribute("role", "");
        }
        return "AssetAllocation/admin/AssetAllocation";
    }


     @GetMapping("/admin/adminCondemn")
    public String AdminCondemn(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("email", user.getEmail());
                String role = user.getRoles().stream().findFirst().map(r -> r.getName().replace("ROLE_", "")).orElse("");
                model.addAttribute("role", role);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("email", "");
                model.addAttribute("role", "");
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("email", "");
            model.addAttribute("role", "");
        }
        return "Condemn/admin/Condemn";
    }

    @GetMapping("/director/Condemn")
    public String DirectorCondemn(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("email", user.getEmail());
                String role = user.getRoles().stream().findFirst().map(r -> r.getName().replace("ROLE_", "")).orElse("");
                model.addAttribute("role", role);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("email", "");
                model.addAttribute("role", "");
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("email", "");
            model.addAttribute("role", "");
        }
        return "Condemn/director/Condemn";
    }



    // @GetMapping("/admin/adminCondemn")
    // public String AdminCondemn(Model model) {
    //     return "Condemn/admin/Condemn";
    // }
    // @GetMapping("/adminInvoice")
    // public String AdminInvoice(Model model) {
    //     return "Invoice/admin/Invoice";
    // }
    @GetMapping("/admin/adminMovement")
    public String AdminMovement(Model model) {
        return "Movement/admin/Movement";
    }
    
    // @GetMapping("/admin/adminMaintain")
    // public String AdminMaintain(Model model) {
    //     return "Maintain/admin/Maintain";
    // }

    //director
    // @GetMapping("/director/directorAsset")
    // public String DirectorAsset(Model model) {
    //     return "Asset/director/Asset";
    // }
    @GetMapping("/director/directorAssetAllocation")
    public String DirectorAssetAllocation(Model model) {
        return "AssetAllocation/director/AssetAllocation";
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

    // @GetMapping("/director/directorMaintain")
    // public String DirectorMaintain(Model model) {
    //     return "Maintain/director/Maintain";
    // }

    
}
