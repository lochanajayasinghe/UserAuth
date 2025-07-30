
package com.example.Login.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Maintain {
    @Id
    private String maintainId;
    private LocalDate maintainsDate;
    private LocalDate nextMaintainDate;
    private String description;
    private float cost;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset asset;

    @Column(nullable = false)
    private boolean isDeleted = false;

    // Getters and Setters
    public String getMaintainId() { return maintainId; }
    public void setMaintainId(String maintainId) { this.maintainId = maintainId; }
    public LocalDate getMaintainsDate() { return maintainsDate; }
    public void setMaintainsDate(LocalDate maintainsDate) { this.maintainsDate = maintainsDate; }
    public LocalDate getNextMaintainDate() { return nextMaintainDate; }
    public void setNextMaintainDate(LocalDate nextMaintainDate) { this.nextMaintainDate = nextMaintainDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public float getCost() { return cost; }
    public void setCost(float cost) { this.cost = cost; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
}
