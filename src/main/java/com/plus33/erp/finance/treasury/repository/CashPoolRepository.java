/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : CashPoolRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPoolController
 * Related Service   : CashPoolService, CashPoolServiceImpl
 * Related Repository: CashPoolRepository
 * Related Entity    : CashPool
 * Related DTO       : N/A
 * Related Mapper    : CashPoolMapper
 * Related DB Table  : cash_pools
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashPoolService, CashPoolServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'cash_pools' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPoolRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cash_pools' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_pools}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CashPoolRepository extends JpaRepository<CashPool, Long> {
    List<CashPool> findByCompanyId(Long companyId);
    List<CashPool> findByActiveTrue();
}