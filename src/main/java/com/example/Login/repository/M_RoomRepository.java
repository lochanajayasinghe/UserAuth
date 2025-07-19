package com.example.Login.repository;

import com.example.Login.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface M_RoomRepository extends JpaRepository<Room, String> {
    // Find room by room name
    java.util.Optional<Room> findByRoomName(String roomName);
}
