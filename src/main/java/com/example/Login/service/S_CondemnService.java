package com.example.Login.service;

import com.example.Login.model.Asset;
import com.example.Login.model.Condemn;
import com.example.Login.repository.S_CondemnRepository;
import com.example.Login.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class S_CondemnService {
    @Autowired
    private S_CondemnRepository condemnRepository;
    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public String condemnAsset(Condemn condemn) {
        Asset asset = assetRepository.findById(condemn.getAsset().getAssetId()).orElse(null);
        if (asset != null) {
            asset.setActivityStatus(false); // Set asset as condemned (off)
            assetRepository.save(asset);
            condemnRepository.save(condemn);
            return "Condemn saved";
        }
        return "Asset not found";
    }
}
