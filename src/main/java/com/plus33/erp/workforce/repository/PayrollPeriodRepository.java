/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollPeriodRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPeriodController
 * Related Service   : PayrollPeriodService, PayrollPeriodServiceImpl
 * Related Repository: PayrollPeriodRepository
 * Related Entity    : PayrollPeriod
 * Related DTO       : N/A
 * Related Mapper    : PayrollPeriodMapper
 * Related DB Table  : payroll_periods
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollPeriodService, PayrollPeriodServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_periods' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollPeriodRepository extends JpaRepository<PayrollPeriod, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(p) > 0 FROM PayrollPeriod p " +
           "WHERE p.startDate <= :endDate AND p.endDate >= :startDate AND p.status IN ('LOCKED', 'CLOSED')")
    boolean existsLockedOrClosedPeriodOverlapping(
            @org.springframework.data.repository.query.Param("startDate") java.time.LocalDate startDate,
            @org.springframework.data.repository.query.Param("endDate") java.time.LocalDate endDate);
}
