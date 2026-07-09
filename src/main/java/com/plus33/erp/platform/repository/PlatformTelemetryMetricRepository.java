/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformTelemetryMetricRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTelemetryMetricController
 * Related Service   : PlatformTelemetryMetricService, PlatformTelemetryMetricServiceImpl
 * Related Repository: PlatformTelemetryMetricRepository
 * Related Entity    : PlatformTelemetryMetric
 * Related DTO       : N/A
 * Related Mapper    : PlatformTelemetryMetricMapper
 * Related DB Table  : platform_telemetry_metrics
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTelemetryMetricService, PlatformTelemetryMetricServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_telemetry_metrics' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTelemetryMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformTelemetryMetricRepository extends JpaRepository<PlatformTelemetryMetric, Long> {
}