package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.InstalledAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstalledAssetRepository extends JpaRepository<InstalledAsset, Long> {
    Optional<InstalledAsset> findBySerialNumber(String serialNumber);
}
