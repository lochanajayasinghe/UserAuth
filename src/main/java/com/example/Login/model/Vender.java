package com.example.Login.model;
import jakarta.persistence.*;

@Entity
public class Vender {
    @Id
    private String venderId;
    private String venderName;
    private String address;
    private int contactNo;

    // Getters and Setters
    public String getVenderId() { return venderId; }
    public void setVenderId(String venderId) { this.venderId = venderId; }
    public String getVenderName() { return venderName; }
    public void setVenderName(String venderName) { this.venderName = venderName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getContactNo() { return contactNo; }
    public void setContactNo(int contactNo) { this.contactNo = contactNo; }
}
