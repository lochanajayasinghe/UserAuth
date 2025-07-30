package com.example.Login.service;

import com.example.Login.model.Invoice;
import com.example.Login.repository.M_InvoiceRepository;
import com.example.Login.repository.VenderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class M_InvoiceService {
    // Return all invoice numbers (non-deleted) as List<String>
    public List<String> getAllInvoiceNumbers() {
        List<Invoice> invoices = getAllInvoices();
        return invoices.stream().map(Invoice::getInvoiceNumber).toList();
    }
    public Invoice getInvoiceById(String invoiceNumber) {
        return invoiceRepository.findById(invoiceNumber).orElse(null);
    }
    // Filtered search for non-deleted invoices
    public List<Invoice> findByInvoiceNumberContaining(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumberContainingAndDeletedFalse(invoiceNumber);
    }
    private final M_InvoiceRepository invoiceRepository;
    private final com.example.Login.repository.VenderRepository venderRepository;

    public M_InvoiceService(M_InvoiceRepository invoiceRepository, com.example.Login.repository.VenderRepository venderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.venderRepository = venderRepository;
    }

    @Transactional
    public Invoice saveInvoice(Invoice invoice, String venderName, String address, int contactNo) {
        // Try to find existing vender
        com.example.Login.model.Vender vender = venderRepository.findByVenderNameAndContactNo(venderName, contactNo)
            .orElseGet(() -> {
                // Create new vender if not found
                com.example.Login.model.Vender v = new com.example.Login.model.Vender();
                v.setVenderName(venderName);
                v.setAddress(address);
                v.setContactNo(contactNo);
                v.setVenderId(java.util.UUID.randomUUID().toString());
                return venderRepository.save(v);
            });
        invoice.setVender(vender);
        return invoiceRepository.save(invoice);
    }

    // Get all non-deleted invoices
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findByDeletedFalse();
    }

    // Get all deleted invoices (recycle bin)
    public List<Invoice> getDeletedInvoices() {
        return invoiceRepository.findByDeletedTrue();
    }

    // Soft delete: set deleted=true
    @Transactional
    public void softDeleteInvoice(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findById(invoiceNumber).orElse(null);
        if (invoice != null) {
            invoice.setDeleted(true);
            invoiceRepository.save(invoice);
        }
    }

    // Restore: set deleted=false
    @Transactional
    public void restoreInvoice(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findById(invoiceNumber).orElse(null);
        if (invoice != null) {
            invoice.setDeleted(false);
            invoiceRepository.save(invoice);
        }
    }

    // Permanent delete
    @Transactional
    public void permanentDeleteInvoice(String invoiceNumber) {
        invoiceRepository.deleteById(invoiceNumber);
    }
}
