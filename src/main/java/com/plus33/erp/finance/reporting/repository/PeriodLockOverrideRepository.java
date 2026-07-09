/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.repository
 * File              : PeriodLockOverrideRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockOverrideController
 * Related Service   : PeriodLockOverrideService, PeriodLockOverrideServiceImpl
 * Related Repository: PeriodLockOverrideRepository
 * Related Entity    : PeriodLockOverride
 * Related DTO       : N/A
 * Related Mapper    : PeriodLockOverrideMapper
 * Related DB Table  : period_lock_overrides
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PeriodLockOverrideService, PeriodLockOverrideServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'period_lock_overrides' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.PeriodLockOverride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodLockOverrideRepository extends JpaRepository<PeriodLockOverride, Long> {
}
