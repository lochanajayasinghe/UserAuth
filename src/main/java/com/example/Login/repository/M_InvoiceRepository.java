package com.example.Login.repository;

import com.example.Login.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface M_InvoiceRepository extends JpaRepository<Invoice, String> {
}
