package com.example.Login.repository;

import com.example.Login.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface M_LocationRepository extends JpaRepository<Location, Long> {
    // You can add custom queries here if needed
}
