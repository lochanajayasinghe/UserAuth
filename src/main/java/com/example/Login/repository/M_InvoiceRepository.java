package com.example.Login.repository;

import com.example.Login.model.Invoice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface M_InvoiceRepository extends JpaRepository<Invoice, String> {
    // Find invoices where invoiceNumber contains the filter string
    List<Invoice> findByInvoiceNumberContaining(String invoiceNumber);
}
