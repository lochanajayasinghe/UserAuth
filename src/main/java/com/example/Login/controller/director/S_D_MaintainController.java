
package com.example.Login.controller.director;

import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;
import org.springframework.security.core.Authentication;

import com.example.Login.model.Maintain;
import com.example.Login.repository.S_MaintainRepository;
import com.example.Login.repository.AssetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/director/directorMaintain")
public class S_D_MaintainController {
    private final UserRepository userRepository;

    public S_D_MaintainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private S_MaintainRepository maintainRepository;

    @Autowired
    private AssetRepository assetRepository;

    // List all maintenance records (not deleted)
    @GetMapping
    public String listMaintains(Model model, Authentication authentication) {
        List<Maintain> maintains = maintainRepository.findAll().stream()
            .filter(m -> !m.isDeleted())
            .toList();
        model.addAttribute("maintains", maintains);
        model.addAttribute("maintain", new Maintain()); // For modal or form
        model.addAttribute("assets", assetRepository.findAll());

        // Add user info for header
        if (authentication != null) {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUsername());
                model.addAttribute("email", user.getEmail());
                String role = user.getRoles().stream().findFirst().map(r -> r.getName().replace("ROLE_", "")).orElse("");
                model.addAttribute("role", role);
            } else {
                model.addAttribute("username", username);
                model.addAttribute("email", "");
                model.addAttribute("role", "");
            }
        } else {
            model.addAttribute("username", "");
            model.addAttribute("email", "");
            model.addAttribute("role", "");
        }

        return "Maintain/director/list";
    }

    // Recycle bin: show deleted records
    @GetMapping("/recycle-bin")
    public String showRecycleBin(Model model) {
        List<Maintain> deletedMaintains = maintainRepository.findAll().stream()
            .filter(Maintain::isDeleted)
            .toList();
        model.addAttribute("deletedMaintains", deletedMaintains);
        return "Maintain/director/recycle-bin";
    }
    // Show form to add new maintenance
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("maintain", new Maintain());
        model.addAttribute("assets", assetRepository.findAll());
        return "Maintain/director/form";
    }

    // Save a maintenance record
    @PostMapping("/save")
    public String saveMaintain(@ModelAttribute Maintain maintain) {
        maintainRepository.save(maintain);
        return "redirect:/director/directorMaintain";
    }

    // Edit a maintenance record
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String maintainId, Model model) {
        Optional<Maintain> maintainOpt = maintainRepository.findById(maintainId);
        if (maintainOpt.isPresent()) {
            model.addAttribute("maintain", maintainOpt.get());
            model.addAttribute("assets", assetRepository.findAll());
            return "Maintain/director/form";
        } else {
            return "redirect:/director/directorMaintain";
        }
    }

    // Soft delete a maintenance record
    @GetMapping("/delete/{id}")
    public String deleteMaintain(@PathVariable("id") String maintainId) {
        Optional<Maintain> maintainOpt = maintainRepository.findById(maintainId);
        if (maintainOpt.isPresent()) {
            Maintain maintain = maintainOpt.get();
            maintain.setDeleted(true);
            maintainRepository.save(maintain);
        }
        return "redirect:/director/directorMaintain";
    }
    // Restore a deleted maintenance record
    @GetMapping("/restore/{id}")
    public String restoreMaintain(@PathVariable("id") String maintainId) {
        Optional<Maintain> maintainOpt = maintainRepository.findById(maintainId);
        if (maintainOpt.isPresent()) {
            Maintain maintain = maintainOpt.get();
            maintain.setDeleted(false);
            maintainRepository.save(maintain);
        }
        return "redirect:/director/directorMaintain/recycle-bin";
    }
    
    // Permanently delete a maintenance record
    @GetMapping("/permanent-delete/{id}")
    public String permanentDeleteMaintain(@PathVariable("id") String maintainId) {
        maintainRepository.deleteById(maintainId);
        return "redirect:/director/directorMaintain/recycle-bin";
    }
}


