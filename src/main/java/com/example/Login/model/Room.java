package com.example.Login.model;
import jakarta.persistence.*;

@Entity
public class Room {
    @Id
    private String roomId;
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;

    // Getters and Setters
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
