package com.example.Login.controller.admin;

import com.example.Login.model.Asset;
import com.example.Login.model.AssetUser;
import com.example.Login.repository.AssetRepository;
import com.example.Login.repository.AssetUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/admin/assetAllocation")
public class L_A_AssetAllocationController {
    // Set end date for asset assignment
    @PostMapping("/endDate")
    public String setEndDate(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String assetId = payload.get("assetId");
        String endDateStr = payload.get("endDate");
        if (userId == null || assetId == null || endDateStr == null) return "Missing data";
        List<AssetUser> assignments = assetUserRepository.findAll().stream()
            .filter(u -> userId.equals(u.getUserId()) && assetId.equals(u.getAsset().getAssetId()) && u.getEndDate() == null)
            .toList();
        if (assignments.isEmpty()) return "Assignment not found";
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date endDate = sdf.parse(endDateStr);
            for (AssetUser u : assignments) {
                u.setEndDate(endDate);
                assetUserRepository.save(u);
            }
            return "Success";
        } catch (Exception e) {
            return "Invalid date";
        }
    }
    @Autowired
    private AssetUserRepository assetUserRepository;
    @Autowired
    private AssetRepository assetRepository;

    // Suggest AssetUser by userName
    @GetMapping("/assetUsers/suggest")
    public List<Map<String, String>> suggestAssetUsers(@RequestParam String query) {
        List<AssetUser> users = assetUserRepository.findByUserNameContainingIgnoreCase(query);
        // Deduplicate by userId and userName
        return users.stream()
                .collect(Collectors.toMap(
                    u -> u.getUserId() + "|" + u.getUserName(),
                    u -> Map.of(
                        "userId", u.getUserId(),
                        "userName", u.getUserName(),
                        "jobRole", u.getJobRole()
                    ),
                    (a, b) -> a
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    // Suggest Asset by assetId or name
    @GetMapping("/assets/suggest")
    public List<Map<String, String>> suggestAssets(@RequestParam String query) {
        List<Asset> assets = assetRepository.findByAssetIdContainingIgnoreCaseOrNameContainingIgnoreCase(query, query);
        return assets.stream()
                .filter(a -> a.isActivityStatus()) // Only suggest assets that are not condemned
                .map(a -> Map.of("assetId", a.getAssetId(), "name", a.getName()))
                .collect(Collectors.toList());
    }
    
    // View all user-asset assignments
    @GetMapping("/all")
    public List<Map<String, String>> getAllAssignments() {
        return assetUserRepository.findAll().stream()
            .filter(u -> u.getAsset() != null && u.getUserId() != null && !u.getUserId().trim().isEmpty())
            .map(u -> {
                String start = u.getStartDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(u.getStartDate()) : "";
                String end = u.getEndDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(u.getEndDate()) : "";
                String timePeriod;
                if (!start.isEmpty() && !end.isEmpty()) {
                    timePeriod = start + " - " + end;
                } else if (!start.isEmpty()) {
                    timePeriod = start + " - Present";
                } else {
                    timePeriod = "";
                }
                return Map.of(
                    "userId", u.getUserId(),
                    "userName", u.getUserName(),
                    "jobRole", u.getJobRole(),
                    "assetId", u.getAsset().getAssetId(),
                    "assetName", u.getAsset().getName(),
                    "timePeriod", timePeriod
                );
            })
            .collect(Collectors.toList());
    }

    // Assign user to asset
    @PostMapping("/assign")
    public String assignUserToAsset(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String userName = payload.get("userName");
        String jobRole = payload.get("jobRole");
        String assetId = payload.get("assetId");
        String startDateStr = payload.get("startDate");
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null) return "Asset not found";
        // Prevent assignment if asset is condemned (activityStatus == false)
        if (!asset.isActivityStatus()) {
            return "Error: This asset is condemned and cannot be assigned.";
        }
        // If asset is already assigned, end previous assignment
        List<AssetUser> currentAssignments = assetUserRepository.findAll().stream()
            .filter(u -> assetId.equals(u.getAsset().getAssetId()) && u.getEndDate() == null)
            .toList();
        if (!currentAssignments.isEmpty()) {
            java.util.Date today = new java.util.Date();
            for (AssetUser u : currentAssignments) {
                u.setEndDate(today);
                assetUserRepository.save(u);
            }
        }
        AssetUser assetUser = new AssetUser();
        assetUser.setUserId(userId);
        assetUser.setUserName(userName);
        assetUser.setJobRole(jobRole);
        assetUser.setAsset(asset);
        if (startDateStr != null && !startDateStr.isEmpty()) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                assetUser.setStartDate(sdf.parse(startDateStr));
            } catch (Exception e) {
                // Invalid date format, ignore or handle as needed
            }
        }
        assetUserRepository.save(assetUser);
        return "Success";
    }
}
