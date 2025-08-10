package com.example.Login.controller;

import com.example.Login.model.Asset;
import com.example.Login.service.H_AssetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;

@Controller
public class H_AssetController {
    private final H_AssetService assetService;
    private final UserRepository userRepository;

    public H_AssetController(H_AssetService assetService, UserRepository userRepository) {
        this.assetService = assetService;
        this.userRepository = userRepository;
    }
    // Show all assets and provide empty asset for modal form
    @GetMapping("/Assets")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String showAssets(Model model, Authentication authentication) {
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("asset", new Asset());
        if (authentication != null) {
            String uname = authentication.getName();
            User user = userRepository.findByUsername(uname).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("profilePhotoUrl", user.getProfilePhotoUrl());
            } else {
                model.addAttribute("username", uname);
                model.addAttribute("profilePhotoUrl", null);
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("profilePhotoUrl", null);
        }
        return "Asset/AddAsset";
    }

    // Handle asset add from modal form
    @PostMapping("/Assets")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String addAsset(@ModelAttribute("asset") Asset asset, Model model, Authentication authentication) {
        assetService.saveAsset(asset);
        model.addAttribute("success", true);
        // After adding, reload all assets and show success
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("asset", new Asset());
        if (authentication != null) {
            String uname = authentication.getName();
            User user = userRepository.findByUsername(uname).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("profilePhotoUrl", user.getProfilePhotoUrl());
            } else {
                model.addAttribute("username", uname);
                model.addAttribute("profilePhotoUrl", null);
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("profilePhotoUrl", null);
        }
        return "Asset/AddAsset";
    }

}
