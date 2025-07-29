package com.example.Login.repository;

import com.example.Login.model.Invoice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface M_InvoiceRepository extends JpaRepository<Invoice, String> {
    // Find invoices where invoiceNumber contains the filter string and not deleted
    List<Invoice> findByInvoiceNumberContainingAndDeletedFalse(String invoiceNumber);

    // Find all non-deleted invoices
    List<Invoice> findByDeletedFalse();

    // Find all deleted invoices
    List<Invoice> findByDeletedTrue();
}
