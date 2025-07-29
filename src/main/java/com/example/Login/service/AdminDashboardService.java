package com.example.Login.service;

import com.example.Login.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AdminDashboardService {

    public java.util.List<com.example.Login.model.User> getRecentUsers() {
        // If you have a createdAt field, use: findTop4ByOrderByCreatedAtDesc()
        // Otherwise, just return the latest 4 by id (descending)
        return userRepository.findTop4ByOrderByIdDesc();
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private H_AssetRepository assetRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private L_StaffRepository staffRepository;
    @Autowired
    private S_CondemnRepository condemnRepository;

    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("userCount", userRepository.count());
        metrics.put("assetCount", assetRepository.countByDeletedFalse());
        metrics.put("invoiceCount", invoiceRepository.count());
        metrics.put("staffCount", staffRepository.count());
        metrics.put("condemnCount", condemnRepository.count());
        return metrics;
    }

    public Map<String, Object> getRecentActivity() {
        Map<String, Object> activity = new HashMap<>();
        activity.put("recentAssets", assetRepository.findTop5ByOrderByPurchaseDateDesc());
        activity.put("recentInvoices", invoiceRepository.findTop5ByOrderByInvoiceDateDesc());
        return activity;
    }

    public Map<String, Object> getAssetDistribution() {
        Map<String, Object> result = new HashMap<>();
        result.put("distribution", assetRepository.getAssetDistributionByType());
        return result;
    }
}
