
package com.example.Login.service;

import com.example.Login.model.Asset;
import com.example.Login.repository.H_AssetRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class H_AssetService {
    private final H_AssetRepository assetRepository;

    public H_AssetService(H_AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset getAssetById(String assetId) {
        return assetRepository.findById(assetId).orElse(null);
    }

    public Asset saveAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findByDeletedFalse();
    }

    public List<Asset> getDeletedAssets() {
        return assetRepository.findByDeletedTrue();
    }

    public void deleteAssetPermanently(String assetId) {
        assetRepository.deleteById(assetId);
    }

    public void restoreAsset(String assetId) {
        Asset asset = getAssetById(assetId);
        if (asset != null && asset.isDeleted()) {
            asset.setDeleted(false);
            saveAsset(asset);
        }
    }
}
