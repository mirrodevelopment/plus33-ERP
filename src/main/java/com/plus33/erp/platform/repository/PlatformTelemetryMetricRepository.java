package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTelemetryMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformTelemetryMetricRepository extends JpaRepository<PlatformTelemetryMetric, Long> {
}