/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformScadaAuditTrailRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaAuditTrailController
 * Related Service   : PlatformScadaAuditTrailService, PlatformScadaAuditTrailServiceImpl
 * Related Repository: PlatformScadaAuditTrailRepository
 * Related Entity    : PlatformScadaAuditTrail
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaAuditTrailMapper
 * Related DB Table  : platform_scada_audit_trails
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaAuditTrailService, PlatformScadaAuditTrailServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_scada_audit_trails' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformScadaAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaAuditTrailRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_scada_audit_trails' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_audit_trails}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformScadaAuditTrailRepository extends JpaRepository<PlatformScadaAuditTrail, Long> {
}