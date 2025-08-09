
package com.example.Login.controller.director;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Login.model.Asset;
import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;
import com.example.Login.service.H_AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/director")
public class H_D_AssetController {
    private final H_AssetService assetService;

    @Autowired
    private com.example.Login.service.M_InvoiceService invoiceService; // Service to fetch invoice numbers

    public H_D_AssetController(H_AssetService assetService) {
        this.assetService = assetService;
    }

    // Soft delete asset by id
    @PostMapping("/directorAsset/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_director', 'ROLE_DIRECTOR')")
    public String softDeleteAsset(@PathVariable("id") String assetId, Model model) {
        Asset asset = assetService.getAssetById(assetId);
        if (asset != null) {
            asset.setDeleted(true);
            assetService.saveAsset(asset);
        }
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("success", true);
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/director/AddAsset";
    }

    // Restore soft deleted asset
    @PostMapping("/directorAsset/restore/{id}")
    @PreAuthorize("hasAnyRole('ROLE_director', 'ROLE_DIRECTOR')")
    public String restoreAsset(@PathVariable("id") String assetId, Model model) {
        assetService.restoreAsset(assetId);
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/director/AddAsset";
    }

    // Permanently delete asset
    @PostMapping("/directorAsset/permanent-delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_director', 'ROLE_DIRECTOR')")
    public String permanentDeleteAsset(@PathVariable("id") String assetId, Model model) {
        assetService.deleteAssetPermanently(assetId);
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/director/AddAsset";
    }
    // Show all assets and provide empty asset for modal form, also add user info for header
    @GetMapping("/directorAsset")
    @PreAuthorize("hasAnyRole('ROLE_director', 'ROLE_DIRECTOR')")
    public String showAssets(Model model, Authentication authentication) {
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
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
        return "Asset/director/AddAsset";
    }

    // Handle asset add from modal form
    @PostMapping("/directorAsset")
    @PreAuthorize("hasAnyRole('ROLE_director', 'ROLE_DIRECTOR')")
    public String addAsset(@ModelAttribute("asset") Asset asset, Model model) {
        assetService.saveAsset(asset);
        model.addAttribute("success", true);
        // After adding, reload all assets and show success
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/director/AddAsset";
    }
    @Autowired
    private UserRepository userRepository;

}
