package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformCapacityForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformCapacityForecastRepository extends JpaRepository<PlatformCapacityForecast, Long> {
}