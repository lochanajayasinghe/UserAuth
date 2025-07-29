package com.example.Login.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Invoice {
    @Id
    private String invoiceNumber;
    private String invoiceId;
    private String orderId; // <-- new field
    private java.time.LocalDate invoiceDate;
    private int itemCount;
    private String invoiceCost;

    // Soft delete flag
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "venderId")
    private Vender vender;

    // Transient fields for form binding
    @Transient
    private String venderName;
    @Transient
    private String address;
    @Transient
    private int contactNo;

    // Getters and Setters
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public java.time.LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(java.time.LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public Vender getVender() { return vender; }
    public void setVender(Vender vender) { this.vender = vender; }
    public String getInvoiceCost() { return invoiceCost; }
    public void setInvoiceCost(String invoiceCost) { this.invoiceCost = invoiceCost; }

    public String getVenderName() { return venderName; }
    public void setVenderName(String venderName) { this.venderName = venderName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getContactNo() { return contactNo; }
    public void setContactNo(int contactNo) { this.contactNo = contactNo; }

    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
}
