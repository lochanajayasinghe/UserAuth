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
@RequestMapping("assetAllocation")
public class AssetAllocationController {
    @Autowired
    private AssetUserRepository assetUserRepository;
    @Autowired
    private AssetRepository assetRepository;

    // Suggest AssetUser by userName
    @GetMapping("/assetUsers/suggest")
    public List<Map<String, String>> suggestAssetUsers(@RequestParam String query) {
        List<AssetUser> users = assetUserRepository.findByUserNameContainingIgnoreCase(query);
        return users.stream()
                .map(u -> Map.of("userName", u.getUserName(), "jobRole", u.getJobRole()))
                .collect(Collectors.toList());
    }

    // Suggest Asset by assetId or name
    @GetMapping("/assets/suggest")
    public List<Map<String, String>> suggestAssets(@RequestParam String query) {
        List<Asset> assets = assetRepository.findByAssetIdContainingIgnoreCaseOrNameContainingIgnoreCase(query, query);
        return assets.stream()
                .map(a -> Map.of("assetId", a.getAssetId(), "name", a.getName()))
                .collect(Collectors.toList());
    }

    // Assign user to asset
    @PostMapping("/assign")
    public String assignUserToAsset(@RequestBody Map<String, String> payload) {
        String userName = payload.get("userName");
        String jobRole = payload.get("jobRole");
        String assetId = payload.get("assetId");
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null) return "Asset not found";
        AssetUser assetUser = new AssetUser();
        assetUser.setUserName(userName);
        assetUser.setJobRole(jobRole);
        assetUser.setAsset(asset);
        assetUserRepository.save(assetUser);
        return "Success";
    }
}
