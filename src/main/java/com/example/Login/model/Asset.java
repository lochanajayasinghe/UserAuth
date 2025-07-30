package com.example.Login.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Asset {
    @Id
    private String assetId;
    private String name;
    private String type;
    private String serialNumber;
    private String model;
    private String specification;
    private java.time.LocalDate purchaseDate;
    @Column(name = "activity_status")
    private boolean activityStatus = true;
    private String warrantyId;
    private java.time.LocalDate warrantyDate;
    private String warrantyPeriod;
    private String purchaseStore;

    // Add invoiceNumber field
    @Column(name = "invoice_number")
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @Column(name = "deleted")
    private boolean deleted = false;
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Getters and Setters
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }

    public java.time.LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(java.time.LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public boolean isActivityStatus() { return activityStatus; }
    public void setActivityStatus(boolean activityStatus) { this.activityStatus = activityStatus; }

    public String getWarrantyId() { return warrantyId; }
    public void setWarrantyId(String warrantyId) { this.warrantyId = warrantyId; }

    public java.time.LocalDate getWarrantyDate() { return warrantyDate; }
    public void setWarrantyDate(java.time.LocalDate warrantyDate) { this.warrantyDate = warrantyDate; }

    public String getWarrantyPeriod() { return warrantyPeriod; }
    public void setWarrantyPeriod(String warrantyPeriod) { this.warrantyPeriod = warrantyPeriod; }

    public String getPurchaseStore() { return purchaseStore; }
    public void setPurchaseStore(String purchaseStore) { this.purchaseStore = purchaseStore; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}


