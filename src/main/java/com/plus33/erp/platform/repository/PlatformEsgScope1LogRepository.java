/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformEsgScope1LogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEsgScope1LogController
 * Related Service   : PlatformEsgScope1LogService, PlatformEsgScope1LogServiceImpl
 * Related Repository: PlatformEsgScope1LogRepository
 * Related Entity    : PlatformEsgScope1Log
 * Related DTO       : N/A
 * Related Mapper    : PlatformEsgScope1LogMapper
 * Related DB Table  : platform_esg_scope1_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEsgScope1LogService, PlatformEsgScope1LogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_esg_scope1_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformEsgScope1Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEsgScope1LogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_esg_scope1_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_esg_scope1_logs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformEsgScope1LogRepository extends JpaRepository<PlatformEsgScope1Log, Long> {
}