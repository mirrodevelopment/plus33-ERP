/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : ProcurementAnalyticsSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementAnalyticsSnapshotController
 * Related Service   : ProcurementAnalyticsSnapshotService, ProcurementAnalyticsSnapshotServiceImpl
 * Related Repository: ProcurementAnalyticsSnapshotRepository
 * Related Entity    : ProcurementAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : ProcurementAnalyticsSnapshotMapper
 * Related DB Table  : procurement_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementAnalyticsSnapshotService, ProcurementAnalyticsSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'procurement_analytics_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementAnalyticsSnapshotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'procurement_analytics_snapshots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_analytics_snapshots}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProcurementAnalyticsSnapshotRepository extends JpaRepository<ProcurementAnalyticsSnapshot, Long> {
    List<ProcurementAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
