package com.example.Login.controller.admin;

import com.example.Login.dto.StaffDto;
import com.example.Login.service.L_StaffService;
import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class L_A_StaffController {
    private final L_StaffService staffService;
    private final UserRepository userRepository;

    public L_A_StaffController(L_StaffService staffService, UserRepository userRepository) {
        this.staffService = staffService;
        this.userRepository = userRepository;
    }

    @GetMapping("/adminStaff")
    public String getStaffList(Model model, Authentication authentication) {
        List<StaffDto> staffList = staffService.getAllStaff();
        // Defensive: ensure staffList is not null
        if (staffList == null) staffList = new java.util.ArrayList<>();
        // Filter to unique userId and sort by userId
        java.util.Map<String, StaffDto> uniqueMap = new java.util.LinkedHashMap<>();
        for (StaffDto staff : staffList) {
            if (staff != null && staff.getUserId() != null && !uniqueMap.containsKey(staff.getUserId())) {
                uniqueMap.put(staff.getUserId(), staff);
            }
        }
        java.util.List<StaffDto> uniqueStaffList = new java.util.ArrayList<>(uniqueMap.values());
        uniqueStaffList.sort(java.util.Comparator.comparing(
            s -> s != null && s.getUserId() != null ? s.getUserId() : "",
            String.CASE_INSENSITIVE_ORDER));

        // Add user info for header
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

        model.addAttribute("uniqueStaffList", uniqueStaffList);
        return "Staff/admin/StaffList";
    }

    @PostMapping("/adminStaff/add")
    @ResponseBody
    public String addStaff(@RequestBody StaffDto dto) {
        boolean success = staffService.addStaff(dto);
        if (success) {
            return "OK";
        } else {
            return "DUPLICATE_USERID";
        }
    }
}
