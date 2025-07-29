package com.example.Login.controller;

import com.example.Login.service.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class AdminDashboardController {

    @GetMapping("/api/admin/dashboard/recent-users")
    public java.util.List<com.example.Login.model.User> getRecentUsers() {
        return dashboardService.getRecentUsers();
    }
    @Autowired
    private AdminDashboardService dashboardService;

    @GetMapping("/api/admin/dashboard/metrics")
    public Map<String, Object> getDashboardMetrics() {
        return dashboardService.getDashboardMetrics();
    }

    @GetMapping("/api/admin/dashboard/activity")
    public Map<String, Object> getRecentActivity() {
        return dashboardService.getRecentActivity();
    }

    @GetMapping("/api/admin/dashboard/asset-distribution")
    public Map<String, Object> getAssetDistribution() {
        return dashboardService.getAssetDistribution();
    }
}
