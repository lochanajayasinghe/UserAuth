package com.example.Login.model; 
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Maintain {
    @Id
    private String maintainId;
    private Date maintainsDate;
    private Date nextMaintainDate;
    private String description;
    private float cost;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;

    // Getters and Setters
    public String getMaintainId() { return maintainId; }
    public void setMaintainId(String maintainId) { this.maintainId = maintainId; }
    public Date getMaintainsDate() { return maintainsDate; }
    public void setMaintainsDate(Date maintainsDate) { this.maintainsDate = maintainsDate; }
    public Date getNextMaintainDate() { return nextMaintainDate; }
    public void setNextMaintainDate(Date nextMaintainDate) { this.nextMaintainDate = nextMaintainDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public float getCost() { return cost; }
    public void setCost(float cost) { this.cost = cost; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
}
