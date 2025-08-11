package com.example.Login.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@Controller
public class ProfileController {

    private final ObjectProvider<UserProfileService> userProfileServiceProvider;

    public ProfileController(ObjectProvider<UserProfileService> userProfileServiceProvider) {
        this.userProfileServiceProvider = userProfileServiceProvider;
    }

    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads", "profile");
    private static final List<String> ALLOWED_EXTS = Arrays.asList("png", "jpg", "jpeg", "webp");

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "User";
        Collection<? extends GrantedAuthority> roles = authentication != null
                ? authentication.getAuthorities()
                : Collections.emptyList();

        // Defaults
        String fullName = username;
        String email = username;
        String phone = "";
        String description = "";
        String photoUrl = null;

        // Try DB provider if available
        UserProfileService svc = userProfileServiceProvider != null ? userProfileServiceProvider.getIfAvailable() : null;
        if (svc != null && username != null) {
            ProfileData data = svc.findByUsername(username);
            if (data != null) {
                if (data.getFullName() != null) fullName = data.getFullName();
                if (data.getEmail() != null) email = data.getEmail();
                if (data.getPhone() != null) phone = data.getPhone();
                if (data.getDescription() != null) description = data.getDescription();
                if (data.getProfilePhotoUrl() != null) photoUrl = data.getProfilePhotoUrl();
            }
        }

        model.addAttribute("username", username);
        model.addAttribute("fullName", model.containsAttribute("fullName") ? model.getAttribute("fullName") : fullName);
        model.addAttribute("email", model.containsAttribute("email") ? model.getAttribute("email") : email);
        model.addAttribute("roles", roles);
        model.addAttribute("phone", model.containsAttribute("phone") ? model.getAttribute("phone") : phone);
        model.addAttribute("description", model.containsAttribute("description") ? model.getAttribute("description") : description);

        if (photoUrl != null) {
            model.addAttribute("profilePhotoUrl", photoUrl);
        } else {
            setPhotoUrlIfExists(model, username);
        }

        return "user/profile";
    }

    @PostMapping("/profile/photo")
    public String uploadPhoto(@RequestParam("photo") MultipartFile photo, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/profile?error=Not%20authenticated";
        }
        String username = authentication.getName();

        if (photo == null || photo.isEmpty()) {
            return "redirect:/profile?error=No%20file%20selected";
        }
        String original = StringUtils.cleanPath(Objects.requireNonNull(photo.getOriginalFilename()));
        String ext = getExtension(original).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTS.contains(ext)) {
            return "redirect:/profile?error=Unsupported%20file%20type";
        }

        try {
            Files.createDirectories(UPLOAD_DIR);
            // Delete existing files for this user regardless of ext
            deleteExistingForUser(username);
            Path dest = UPLOAD_DIR.resolve(username + "." + ext);
            Files.copy(photo.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            return "redirect:/profile?success=Profile%20photo%20updated";
        } catch (IOException e) {
            return "redirect:/profile?error=Upload%20failed";
        }
    }

    @PostMapping("/profile/photo/delete")
    public String deletePhoto(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/profile?error=Not%20authenticated";
        }
        String username = authentication.getName();
        try {
            deleteExistingForUser(username);
            return "redirect:/profile?success=Profile%20photo%20deleted";
        } catch (IOException e) {
            return "redirect:/profile?error=Delete%20failed";
        }
    }

    @GetMapping("/profile/photo/{username}")
    @ResponseBody
    public ResponseEntity<Resource> servePhoto(@PathVariable("username") String username) throws IOException {
        Optional<Path> photo = findExistingPhoto(username);
        if (photo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Path path = photo.get();
        Resource resource = toResource(path);
        String contentType = Files.probeContentType(path);
        if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                .body(resource);
    }

    // Helpers
    private void setPhotoUrlIfExists(Model model, String username) {
        try {
            Optional<Path> p = findExistingPhoto(username);
            if (p.isPresent()) {
                long v = Files.getLastModifiedTime(p.get()).toMillis();
                model.addAttribute("profilePhotoUrl", "/profile/photo/" + username + "?v=" + v);
            } else {
                model.addAttribute("profilePhotoUrl", null);
            }
        } catch (IOException e) {
            model.addAttribute("profilePhotoUrl", null);
        }
    }

    private Optional<Path> findExistingPhoto(String username) throws IOException {
        if (!Files.exists(UPLOAD_DIR)) return Optional.empty();
        // Try common extensions first
        for (String ext : ALLOWED_EXTS) {
            Path p = UPLOAD_DIR.resolve(username + "." + ext);
            if (Files.exists(p)) return Optional.of(p);
        }
        // Fallback: scan directory
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(UPLOAD_DIR, username + ".*")) {
            for (Path p : ds) {
                if (Files.isRegularFile(p)) return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    private void deleteExistingForUser(String username) throws IOException {
        if (!Files.exists(UPLOAD_DIR)) return;
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(UPLOAD_DIR, username + ".*")) {
            for (Path p : ds) {
                Files.deleteIfExists(p);
            }
        }
    }

    private static String getExtension(String filename) {
        int idx = filename.lastIndexOf('.')
                ;
        return (idx != -1 && idx < filename.length() - 1) ? filename.substring(idx + 1) : "";
    }

    private static Resource toResource(Path path) throws MalformedURLException {
        return new UrlResource(path.toUri());
    }

    // Pluggable service to fetch user details from DB
    public interface UserProfileService {
        ProfileData findByUsername(String username);
    }

    public static class ProfileData {
        private final String fullName;
        private final String email;
        private final String phone;
        private final String description;
        private final String profilePhotoUrl;

        public ProfileData(String fullName, String email, String phone, String description, String profilePhotoUrl) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.description = description;
            this.profilePhotoUrl = profilePhotoUrl;
        }

        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getDescription() { return description; }
        public String getProfilePhotoUrl() { return profilePhotoUrl; }
    }
}
