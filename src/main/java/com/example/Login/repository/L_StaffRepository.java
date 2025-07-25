package com.example.Login.repository;

import com.example.Login.model.AssetUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface L_StaffRepository extends JpaRepository<AssetUser, Long> {
    // No extra methods needed for basic CRUD
}
