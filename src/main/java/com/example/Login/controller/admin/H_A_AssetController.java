
package com.example.Login.controller.admin;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Login.model.Asset;
import com.example.Login.service.H_AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class H_A_AssetController {
    private final H_AssetService assetService;

    @Autowired
    private com.example.Login.service.M_InvoiceService invoiceService; // Service to fetch invoice numbers

    public H_A_AssetController(H_AssetService assetService) {
        this.assetService = assetService;
    }

    // Soft delete asset by id
    @PostMapping("/adminAsset/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
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
        return "Asset/admin/AddAsset";
    }

    // Restore soft deleted asset
    @PostMapping("/adminAsset/restore/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String restoreAsset(@PathVariable("id") String assetId, Model model) {
        assetService.restoreAsset(assetId);
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/admin/AddAsset";
    }

    // Permanently delete asset
    @PostMapping("/adminAsset/permanent-delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String permanentDeleteAsset(@PathVariable("id") String assetId, Model model) {
        assetService.deleteAssetPermanently(assetId);
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/admin/AddAsset";
    }
    // Show all assets and provide empty asset for modal form
    @GetMapping("/adminAsset")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String showAssets(Model model) {
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/admin/AddAsset";
    }

    // Handle asset add from modal form
    @PostMapping("/adminAsset")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String addAsset(@ModelAttribute("asset") Asset asset, Model model) {
        assetService.saveAsset(asset);
        model.addAttribute("success", true);
        // After adding, reload all assets and show success
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("deletedAssets", assetService.getDeletedAssets());
        model.addAttribute("asset", new Asset());
        model.addAttribute("invoiceNumbers", invoiceService.getAllInvoiceNumbers());
        return "Asset/admin/AddAsset";
    }

}
