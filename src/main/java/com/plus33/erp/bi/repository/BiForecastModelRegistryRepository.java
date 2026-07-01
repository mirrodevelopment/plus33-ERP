package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiForecastModelRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiForecastModelRegistryRepository extends JpaRepository<BiForecastModelRegistry, Long> {
    java.util.List<BiForecastModelRegistry> findByIsActiveTrueAndForecastDomain(String domain);
    java.util.Optional<BiForecastModelRegistry> findByModelCode(String modelCode);
}
