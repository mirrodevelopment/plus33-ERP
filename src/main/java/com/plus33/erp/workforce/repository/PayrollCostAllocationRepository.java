/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollCostAllocationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollCostAllocationController
 * Related Service   : PayrollCostAllocationService, PayrollCostAllocationServiceImpl
 * Related Repository: PayrollCostAllocationRepository
 * Related Entity    : PayrollCostAllocation
 * Related DTO       : N/A
 * Related Mapper    : PayrollCostAllocationMapper
 * Related DB Table  : payroll_cost_allocations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollCostAllocationService, PayrollCostAllocationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_cost_allocations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollCostAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollCostAllocationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_cost_allocations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_cost_allocations}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollCostAllocationRepository extends JpaRepository<PayrollCostAllocation, Long> {
    List<PayrollCostAllocation> findByPayrollRunItemId(Long payrollRunItemId);
}