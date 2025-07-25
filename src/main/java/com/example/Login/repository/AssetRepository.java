package com.example.Login.repository;

import com.example.Login.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, String> {
    List<Asset> findByAssetIdContainingIgnoreCaseOrNameContainingIgnoreCase(String assetId, String name);
}
