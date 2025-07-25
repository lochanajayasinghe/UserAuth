package com.example.Login.repository;

import com.example.Login.model.AssetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetUserRepository extends JpaRepository<AssetUser, Long> {
    List<AssetUser> findByUserNameContainingIgnoreCase(String userName);
}
