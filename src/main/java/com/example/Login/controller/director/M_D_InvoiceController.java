package com.example.Login.controller.director;

import com.example.Login.model.Invoice;
import com.example.Login.service.M_InvoiceService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/director/directorInvoice")
public class M_D_InvoiceController {
    private final M_InvoiceService invoiceService;

    public M_D_InvoiceController(M_InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public String showInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        return "Invoice/director/Invoice";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DIRECTOR')")
    public String addInvoice(@ModelAttribute("invoice") Invoice invoice, Model model) {
        
        invoiceService.saveInvoice(invoice);
        model.addAttribute("success", true);
        // After adding, reload all assets and show success
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        return "Invoice/director/Invoice";
    }
}
