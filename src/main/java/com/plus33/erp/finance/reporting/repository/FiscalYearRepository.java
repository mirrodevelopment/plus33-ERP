/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.repository
 * File              : FiscalYearRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FiscalYearController
 * Related Service   : FiscalYearService, FiscalYearServiceImpl
 * Related Repository: FiscalYearRepository
 * Related Entity    : FiscalYear
 * Related DTO       : N/A
 * Related Mapper    : FiscalYearMapper
 * Related DB Table  : fiscal_years
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FiscalYearService, FiscalYearServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fiscal_years' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.FiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FiscalYearRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fiscal_years' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fiscal_years}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface FiscalYearRepository extends JpaRepository<FiscalYear, Long> {
    Optional<FiscalYear> findByCompanyIdAndFiscalYear(Long companyId, Integer fiscalYear);
    boolean existsByCompanyIdAndFiscalYearAndStatus(Long companyId, Integer fiscalYear, com.plus33.erp.finance.reporting.entity.FiscalYearStatus status);
}
