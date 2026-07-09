/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.repository
 * File              : PeriodLockRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockController
 * Related Service   : PeriodLockService, PeriodLockServiceImpl
 * Related Repository: PeriodLockRepository
 * Related Entity    : PeriodLock
 * Related DTO       : N/A
 * Related Mapper    : PeriodLockMapper
 * Related DB Table  : period_locks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PeriodLockService, PeriodLockServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'period_locks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.PeriodLock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PeriodLockRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'period_locks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code period_locks}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PeriodLockRepository extends JpaRepository<PeriodLock, Long> {
    Optional<PeriodLock> findByCompanyId(Long companyId);
}
