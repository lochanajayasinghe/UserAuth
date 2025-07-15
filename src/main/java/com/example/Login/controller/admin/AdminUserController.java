package com.example.Login.controller.admin;

import com.example.Login.dto.UserDto;
import com.example.Login.service.AdminUserService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        List<UserDto> users = adminUserService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users"; // This will be the Thymeleaf template name
    }
}