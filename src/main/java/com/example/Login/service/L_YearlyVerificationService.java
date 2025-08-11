package com.example.Login.service;

import com.example.Login.model.Asset;
import com.example.Login.model.YearlyVerification;
import com.example.Login.repository.AssetRepository;
import com.example.Login.repository.L_YearlyVerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class L_YearlyVerificationService {

    private final AssetRepository assetRepository;
    private final L_YearlyVerificationRepository yearlyVerificationRepository;

    public L_YearlyVerificationService(AssetRepository assetRepository,
                                       L_YearlyVerificationRepository yearlyVerificationRepository) {
        this.assetRepository = assetRepository;
        this.yearlyVerificationRepository = yearlyVerificationRepository;
    }

    public static class YearlyVerificationView {
        public String assetId;
        public String assetName;
        public String serialNumber;
        public String model;
        public boolean verified;
        public Integer year;
    }

    public List<YearlyVerificationView> getStatusForYear(int year) {
        List<Asset> activeAssets = assetRepository.findAll().stream()
                .filter(a -> a.isActivityStatus() && !a.isDeleted())
                .collect(Collectors.toList());

        Map<String, YearlyVerification> existing = yearlyVerificationRepository.findAllForActiveAssetsByYear(year)
                .stream().collect(Collectors.toMap(y -> y.getAsset().getAssetId(), y -> y));

        List<YearlyVerificationView> result = new ArrayList<>();
        for (Asset a : activeAssets) {
            YearlyVerificationView v = new YearlyVerificationView();
            v.assetId = a.getAssetId();
            v.assetName = a.getName();
            v.serialNumber = a.getSerialNumber();
            v.model = a.getModel();
            v.year = year;
            v.verified = Optional.ofNullable(existing.get(a.getAssetId()))
                    .map(YearlyVerification::isVerified)
                    .orElse(false);
            result.add(v);
        }
        result.sort(Comparator.comparing(y -> y.assetId));
        return result;
    }

    @Transactional
    public boolean toggleVerification(String assetId, int year, boolean newState, String verifier) {
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null || !asset.isActivityStatus() || asset.isDeleted()) {
            return false;
        }
        YearlyVerification yv = yearlyVerificationRepository.findByAssetAndYear(asset, year)
                .orElseGet(() -> {
                    YearlyVerification created = new YearlyVerification();
                    created.setAsset(asset);
                    created.setYear(year);
                    return created;
                });
        yv.setVerified(newState);
        yv.setVerifiedAt(LocalDateTime.now());
        yv.setVerifier(verifier);
        yearlyVerificationRepository.save(yv);
        return true;
    }
}
