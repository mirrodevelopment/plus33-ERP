/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformAnomalyAlertRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAnomalyAlertController
 * Related Service   : PlatformAnomalyAlertService, PlatformAnomalyAlertServiceImpl
 * Related Repository: PlatformAnomalyAlertRepository
 * Related Entity    : PlatformAnomalyAlert
 * Related DTO       : N/A
 * Related Mapper    : PlatformAnomalyAlertMapper
 * Related DB Table  : platform_anomaly_alerts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAnomalyAlertService, PlatformAnomalyAlertServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_anomaly_alerts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformAnomalyAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAnomalyAlertRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_anomaly_alerts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_anomaly_alerts}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformAnomalyAlertRepository extends JpaRepository<PlatformAnomalyAlert, Long> {
}