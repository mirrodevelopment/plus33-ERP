/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiAnalyticsSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAnalyticsSnapshotController
 * Related Service   : BiAnalyticsSnapshotService, BiAnalyticsSnapshotServiceImpl
 * Related Repository: BiAnalyticsSnapshotRepository
 * Related Entity    : BiAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : BiAnalyticsSnapshotMapper
 * Related DB Table  : bi_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAnalyticsSnapshotService, BiAnalyticsSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_analytics_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiAnalyticsSnapshotRepository extends JpaRepository<BiAnalyticsSnapshot, Long> {
    java.util.List<BiAnalyticsSnapshot> findByCompanyIdAndKpiCodeOrderBySnapshotDateDesc(Long companyId, String kpiCode);
    java.util.List<BiAnalyticsSnapshot> findByCompanyIdAndSnapshotDateBetween(Long companyId, java.time.LocalDate from, java.time.LocalDate to);
}
