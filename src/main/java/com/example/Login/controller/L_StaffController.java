package com.example.Login.controller;

import com.example.Login.dto.StaffDto;
import com.example.Login.service.L_StaffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
public class L_StaffController {
    private final L_StaffService staffService;

    public L_StaffController(L_StaffService staffService) {
        this.staffService = staffService;
    }

@GetMapping("/Staff")
    public String getStaffList(Model model) {
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
        model.addAttribute("uniqueStaffList", uniqueStaffList);
        return "Staff/StaffList";
    }

    @PostMapping("/Staff/add")
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
