/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : EsmAnalyticsSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmAnalyticsSnapshotController
 * Related Service   : EsmAnalyticsSnapshotService, EsmAnalyticsSnapshotServiceImpl
 * Related Repository: EsmAnalyticsSnapshotRepository
 * Related Entity    : EsmAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : EsmAnalyticsSnapshotMapper
 * Related DB Table  : esm_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmAnalyticsSnapshotService, EsmAnalyticsSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'esm_analytics_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.EsmAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmAnalyticsSnapshotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'esm_analytics_snapshots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_analytics_snapshots}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EsmAnalyticsSnapshotRepository extends JpaRepository<EsmAnalyticsSnapshot, Long> {
    List<EsmAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
