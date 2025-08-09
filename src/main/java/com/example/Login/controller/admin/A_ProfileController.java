package com.example.Login.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

import com.example.Login.repository.UserRepository;
import com.example.Login.model.User;

@Controller
public class A_ProfileController {

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser(Authentication authentication) {
        String principal = authentication != null ? authentication.getName() : null;
        if (principal == null) return null;
        return userRepository.findByUsername(principal)
                .orElseGet(() -> userRepository.findByEmail(principal).orElse(null));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/profile")
    public String showProfile(Model model, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("fullName", user.getFullName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("description", user.getDescription());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("roles", user.getRoles());
            model.addAttribute("profilePhotoUrl", user.getProfilePhotoUrl());
        } else {
            model.addAttribute("username", "not found");
            model.addAttribute("fullName", "not found");
            model.addAttribute("email", "not found");
            model.addAttribute("description", "not found");
            model.addAttribute("phone", "not found");
            model.addAttribute("roles", null);
            model.addAttribute("profilePhotoUrl", null);
        }
        return "admin/profile";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadProfilePhoto(@RequestParam("photo") MultipartFile photo, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/admin/profile?error=User+not+found";
        }
        if (photo == null || photo.isEmpty()) {
            return "redirect:/admin/profile?error=No+file+selected";
        }
        String contentType = photo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return "redirect:/admin/profile?error=Only+image+files+are+allowed";
        }
        try {
            Path uploadDir = Paths.get("uploads", "profile-photos");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String original = photo.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            } else if ("image/jpeg".equals(contentType)) {
                ext = ".jpg";
            } else if ("image/png".equals(contentType)) {
                ext = ".png";
            } else if ("image/gif".equals(contentType)) {
                ext = ".gif";
            } else if ("image/webp".equals(contentType)) {
                ext = ".webp";
            }
            String filename = "user-" + (user.getId() != null ? user.getId() : user.getUsername()) + "-" + Instant.now().toEpochMilli() + ext;
            Path target = uploadDir.resolve(filename);
            Files.copy(photo.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // delete old file if previously set
            String oldUrl = user.getProfilePhotoUrl();
            if (oldUrl != null && oldUrl.startsWith("/uploads/profile-photos/")) {
                try {
                    Path oldPath = Paths.get(oldUrl.replaceFirst("^/", ""));
                    Files.deleteIfExists(oldPath);
                } catch (Exception ignored) {}
            }

            user.setProfilePhotoUrl("/uploads/profile-photos/" + filename);
            userRepository.save(user);
            return "redirect:/admin/profile?success=Profile+photo+updated";
        } catch (IOException e) {
            return "redirect:/admin/profile?error=Failed+to+save+file";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/profile/photo/delete")
    public String deleteProfilePhoto(Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user == null) {
            return "redirect:/admin/profile?error=User+not+found";
        }
        String oldUrl = user.getProfilePhotoUrl();
        if (oldUrl != null && oldUrl.startsWith("/uploads/profile-photos/")) {
            try {
                Path oldPath = Paths.get(oldUrl.replaceFirst("^/", ""));
                Files.deleteIfExists(oldPath);
            } catch (Exception ignored) {}
        }
        user.setProfilePhotoUrl(null);
        userRepository.save(user);
        return "redirect:/admin/profile?success=Profile+photo+removed";
    }
}
