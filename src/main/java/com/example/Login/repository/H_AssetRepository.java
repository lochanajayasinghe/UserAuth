
package com.example.Login.repository;

import com.example.Login.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface H_AssetRepository extends JpaRepository<Asset, String> {
    // For asset auto-suggest (case-insensitive contains)
    List<Asset> findByAssetIdContainingIgnoreCaseOrNameContainingIgnoreCase(String assetId, String name);

    // Only fetch assets that are not soft deleted
    List<Asset> findByDeletedFalse();

    // Fetch only soft deleted assets
    List<Asset> findByDeletedTrue();

    // Count assets that are not deleted
    long countByDeletedFalse();

    // Get top 5 recently created assets (for dashboard activity)
    List<Asset> findTop5ByOrderByPurchaseDateDesc();

    // Group by type for asset distribution (for pie chart)
    @org.springframework.data.jpa.repository.Query("SELECT a.type as type, COUNT(a) as count FROM Asset a WHERE a.deleted = false GROUP BY a.type")
    List<TypeCount> getAssetDistributionByType();

    interface TypeCount {
        String getType();
        long getCount();
    }
}
