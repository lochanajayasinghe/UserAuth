
package com.example.Login.controller;

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
@RequestMapping("/Maintain")
public class S_MaintainController {

    @Autowired
    private S_MaintainRepository maintainRepository;

    @Autowired
    private AssetRepository assetRepository;

    // List all maintenance records (not deleted)
    @GetMapping
    public String listMaintains(Model model) {
        List<Maintain> maintains = maintainRepository.findAll().stream()
            .filter(m -> !m.isDeleted())
            .toList();
        model.addAttribute("maintains", maintains);
        model.addAttribute("maintain", new Maintain()); // For modal or form
        model.addAttribute("assets", assetRepository.findAll());
        return "Maintain/list";
    }

    // Recycle bin: show deleted records
    @GetMapping("/recycle-bin")
    public String showRecycleBin(Model model) {
        List<Maintain> deletedMaintains = maintainRepository.findAll().stream()
            .filter(Maintain::isDeleted)
            .toList();
        model.addAttribute("deletedMaintains", deletedMaintains);
        return "Maintain/recycle-bin";
    }
    // Show form to add new maintenance
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("maintain", new Maintain());
        model.addAttribute("assets", assetRepository.findAll());
        return "Maintain/form";
    }

    // Save a maintenance record
    @PostMapping("/save")
    public String saveMaintain(@ModelAttribute Maintain maintain) {
        maintainRepository.save(maintain);
        return "redirect:/Maintain";
    }

    // Edit a maintenance record
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String maintainId, Model model) {
        Optional<Maintain> maintainOpt = maintainRepository.findById(maintainId);
        if (maintainOpt.isPresent()) {
            model.addAttribute("maintain", maintainOpt.get());
            model.addAttribute("assets", assetRepository.findAll());
            return "Maintain/form";
        } else {
            return "redirect:/Maintain";
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
        return "redirect:/Maintain";
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
        return "redirect:/Maintain/recycle-bin";
    }
    
    // Permanently delete a maintenance record
    @GetMapping("/permanent-delete/{id}")
    public String permanentDeleteMaintain(@PathVariable("id") String maintainId) {
        maintainRepository.deleteById(maintainId);
        return "redirect:/Maintain/recycle-bin";
    }
}


