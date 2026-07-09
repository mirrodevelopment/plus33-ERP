/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmAnalyticsSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmAnalyticsSnapshotController
 * Related Service   : HcmAnalyticsSnapshotService, HcmAnalyticsSnapshotServiceImpl
 * Related Repository: HcmAnalyticsSnapshotRepository
 * Related Entity    : HcmAnalyticsSnapshot
 * Related DTO       : N/A
 * Related Mapper    : HcmAnalyticsSnapshotMapper
 * Related DB Table  : hcm_analytics_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmAnalyticsSnapshotService, HcmAnalyticsSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_analytics_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmAnalyticsSnapshotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'hcm_analytics_snapshots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_analytics_snapshots}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface HcmAnalyticsSnapshotRepository extends JpaRepository<HcmAnalyticsSnapshot, Long> {
    List<HcmAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
