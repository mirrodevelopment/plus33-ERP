/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxFilingRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxFilingController
 * Related Service   : TaxFilingService, TaxFilingServiceImpl
 * Related Repository: TaxFilingRepository
 * Related Entity    : TaxFiling
 * Related DTO       : N/A
 * Related Mapper    : TaxFilingMapper
 * Related DB Table  : tax_filings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxFilingService, TaxFilingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_filings' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxFiling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxFilingRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_filings' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_filings}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxFilingRepository extends JpaRepository<TaxFiling, Long> {
    Optional<TaxFiling> findByCalendarId(Long calendarId);
}