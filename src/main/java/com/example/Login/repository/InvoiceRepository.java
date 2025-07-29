package com.example.Login.repository;

import com.example.Login.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findTop5ByOrderByInvoiceDateDesc();
}
