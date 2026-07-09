/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : GrcAnalyticsSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GrcAnalyticsSnapshotController
 * Related Service   : GrcAnalyticsSnapshotService, GrcAnalyticsSnapshotServiceImpl
 * Related Repository: GrcAnalyticsSnapshotRepository
 * Related Entity    : GrcAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : GrcAnalyticsSnapshotMapper
 * Related DB Table  : grc_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GrcAnalyticsSnapshotService, GrcAnalyticsSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'grc_analytics_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GrcAnalyticsSnapshotRepository extends JpaRepository<GrcAnalyticsSnapshot, Long> {
    List<GrcAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
