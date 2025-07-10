package com.example.Login.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Condemn {
    @Id
    private String condemnId;
    private Date condemnDate;
    private String condemnDetails;
    private String activityStatus;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;

    // Getters and Setters
    public String getCondemnId() { return condemnId; }
    public void setCondemnId(String condemnId) { this.condemnId = condemnId; }
    public Date getCondemnDate() { return condemnDate; }
    public void setCondemnDate(Date condemnDate) { this.condemnDate = condemnDate; }
    public String getCondemnDetails() { return condemnDetails; }
    public void setCondemnDetails(String condemnDetails) { this.condemnDetails = condemnDetails; }
    public String getActivityStatus() { return activityStatus; }
    public void setActivityStatus(String activityStatus) { this.activityStatus = activityStatus; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
}
