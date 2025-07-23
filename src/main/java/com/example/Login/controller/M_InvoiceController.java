package com.example.Login.controller;

import com.example.Login.model.Invoice;
import com.example.Login.service.M_InvoiceService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Invoice")
public class M_InvoiceController {
    private final M_InvoiceService invoiceService;

    public M_InvoiceController(M_InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public String showInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        return "Invoice/Invoice";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR','ROLE_USER')")
    public String addInvoice(@ModelAttribute("invoice") Invoice invoice, Model model) {
        
        invoiceService.saveInvoice(invoice);
        model.addAttribute("success", true);
        // After adding, reload all assets and show success
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        return "Invoice/Invoice";
    }
}
