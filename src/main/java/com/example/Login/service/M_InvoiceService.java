package com.example.Login.service;

import com.example.Login.model.Invoice;
import com.example.Login.repository.M_InvoiceRepository;
import com.example.Login.repository.VenderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class M_InvoiceService {
    public Invoice getInvoiceById(String invoiceNumber) {
        return invoiceRepository.findById(invoiceNumber).orElse(null);
    }
    public List<Invoice> findByInvoiceNumberContaining(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumberContaining(invoiceNumber);
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

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}
