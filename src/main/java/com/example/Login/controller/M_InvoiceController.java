package com.example.Login.controller;

import com.example.Login.model.Invoice;
import com.example.Login.service.M_InvoiceService;
import com.example.Login.repository.VenderRepository;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Login.model.User;
import com.example.Login.repository.UserRepository;

@Controller
@RequestMapping("/Invoice")
public class M_InvoiceController {
    @GetMapping("/view/{invoiceNumber}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR','ROLE_USER')")
    public String viewInvoice(@PathVariable("invoiceNumber") String invoiceNumber, Model model, Authentication authentication) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceNumber);
        model.addAttribute("invoice", invoice);
        addUserHeader(model, authentication);
        return "Invoice/ViewInvoice";
    }
    private final VenderRepository venderRepository;
    private final M_InvoiceService invoiceService;
    private final UserRepository userRepository;

    public M_InvoiceController(M_InvoiceService invoiceService, VenderRepository venderRepository, UserRepository userRepository) {
        this.invoiceService = invoiceService;
        this.venderRepository = venderRepository;
        this.userRepository = userRepository;
    }
    // Vendor name auto-suggest endpoint
    @GetMapping("/vendors/suggest")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public @ResponseBody List<String> suggestVendors(@RequestParam("query") String query) {
        List<com.example.Login.model.Vender> vendors = venderRepository.findByVenderNameContainingIgnoreCase(query);
        List<String> names = new java.util.ArrayList<>();
        for (com.example.Login.model.Vender v : vendors) {
            names.add(v.getVenderName());
        }
        return names;
    }

    // Vendor details autofill endpoint
    @GetMapping("/vendors/details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR','ROLE_USER')")
    public @ResponseBody java.util.Optional<com.example.Login.model.Vender> getVendorDetails(@RequestParam("venderName") String venderName) {
        return venderRepository.findByVenderName(venderName);
    }

    @GetMapping("")
    public String showInvoices(@RequestParam(value = "invoiceNumberFilter", required = false) String invoiceNumberFilter, Model model, Authentication authentication) {
        if (invoiceNumberFilter != null && !invoiceNumberFilter.isEmpty()) {
            model.addAttribute("invoices", invoiceService.findByInvoiceNumberContaining(invoiceNumberFilter));
        } else {
            model.addAttribute("invoices", invoiceService.getAllInvoices());
        }
        model.addAttribute("invoiceNumberFilter", invoiceNumberFilter);
        model.addAttribute("invoice", new Invoice());
        addUserHeader(model, authentication);
        return "Invoice/Invoice";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String addInvoice(
        @ModelAttribute("invoice") Invoice invoice,
        Model model,
        Authentication authentication) {
        invoiceService.saveInvoice(
            invoice,
            invoice.getVenderName(),
            invoice.getAddress(),
            invoice.getContactNo()
        );
        model.addAttribute("success", true);
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        addUserHeader(model, authentication);
        return "Invoice/Invoice";
    }

    private void addUserHeader(Model model, Authentication authentication) {
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
    }
}
