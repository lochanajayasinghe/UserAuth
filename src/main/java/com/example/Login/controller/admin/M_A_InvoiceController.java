package com.example.Login.controller.admin;

import com.example.Login.model.Invoice;
import com.example.Login.service.M_InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/adminInvoice")
public class M_A_InvoiceController {
    private final M_InvoiceService invoiceService;

    public M_A_InvoiceController(M_InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public String showInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        return "Invoice/admin/Invoice";
    }

    @PostMapping("/add")
    public String addInvoice(@ModelAttribute("invoice") Invoice invoice, Model model) {
        // Auto-generate invoiceNumber if not provided
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
            invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        }

        invoiceService.saveInvoice(invoice);
        model.addAttribute("success", true);
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("invoice", new Invoice());
        return "Invoice/admin/Invoice";
    }
}
