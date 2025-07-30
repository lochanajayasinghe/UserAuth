package com.example.Login.controller;

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
@RequestMapping("/AssetAllocation")
public class L_AssetAllocationController {
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
            .map(u -> Map.of(
                "userId", u.getUserId(),
                "userName", u.getUserName(),
                "jobRole", u.getJobRole(),
                "assetId", u.getAsset().getAssetId(),
                "assetName", u.getAsset().getName()
            ))
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
