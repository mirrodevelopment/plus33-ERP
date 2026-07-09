/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : CostCenterRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostCenterController
 * Related Service   : CostCenterService, CostCenterServiceImpl
 * Related Repository: CostCenterRepository
 * Related Entity    : CostCenter
 * Related DTO       : N/A
 * Related Mapper    : CostCenterMapper
 * Related DB Table  : cost_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CostCenterService, CostCenterServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'cost_centers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CostCenterRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cost_centers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cost_centers}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long>, JpaSpecificationExecutor<CostCenter> {
    Optional<CostCenter> findByCompanyIdAndCode(Long companyId, String code);
}