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

    public Asset saveAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }
}
