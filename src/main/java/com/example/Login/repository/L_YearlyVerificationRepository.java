package com.example.Login.repository;

import com.example.Login.model.YearlyVerification;
import com.example.Login.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface L_YearlyVerificationRepository extends JpaRepository<YearlyVerification, Long> {

    Optional<YearlyVerification> findByAssetAndYear(Asset asset, int year);

    @Query("select y from YearlyVerification y where y.asset.assetId = :assetId and y.year = :year")
    Optional<YearlyVerification> findByAssetIdAndYear(@Param("assetId") String assetId, @Param("year") int year);

    @Query("select y from YearlyVerification y where y.year = :year and y.asset.activityStatus = true and y.asset.deleted = false")
    List<YearlyVerification> findAllForActiveAssetsByYear(@Param("year") int year);
}
