package com.example.Login.controller;

import com.example.Login.service.L_YearlyVerificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Year;
import java.util.Map;

@Controller
public class L_YearlyVerificationController {

    private final L_YearlyVerificationService yearlyVerificationService;

    public L_YearlyVerificationController(L_YearlyVerificationService yearlyVerificationService) {
        this.yearlyVerificationService = yearlyVerificationService;
    }

    @GetMapping("/YearlyVerification")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_USER')")
    public String page(@RequestParam(value = "year", required = false) Integer year,
                       Model model) {
        int y = (year != null) ? year : Year.now().getValue();
        model.addAttribute("year", y);
        model.addAttribute("items", yearlyVerificationService.getStatusForYear(y));
        return "YearlyVerification/YearlyVerification";
    }

    @PostMapping("/YearlyVerification/toggle")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_USER')")
    public Map<String, Object> toggle(@RequestParam("assetId") String assetId,
                                      @RequestParam("year") int year,
                                      @RequestParam("verified") boolean verified,
                                      Principal principal) {
        String user = principal != null ? principal.getName() : "system";
        boolean ok = yearlyVerificationService.toggleVerification(assetId, year, verified, user);
        return Map.of("success", ok);
    }
}
