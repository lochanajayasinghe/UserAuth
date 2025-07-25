package com.example.Login.controller.admin;

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
@RequestMapping("/admin")
public class L_A_StaffController {
    private final L_StaffService staffService;

    public L_A_StaffController(L_StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/adminStaff")
    public String getStaffList(Model model) {
        List<StaffDto> staffList = staffService.getAllStaff();
        model.addAttribute("staffList", staffList);
        return "Staff/admin/StaffList";
    }

    @PostMapping("/adminStaff/add")
    @ResponseBody
    public String addStaff(@RequestBody StaffDto dto) {
        boolean success = staffService.addStaff(dto);
        return success ? "OK" : "ERROR";
    }
}
