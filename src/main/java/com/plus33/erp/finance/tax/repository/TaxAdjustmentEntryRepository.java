/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxAdjustmentEntryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxAdjustmentEntryController
 * Related Service   : TaxAdjustmentEntryService, TaxAdjustmentEntryServiceImpl
 * Related Repository: TaxAdjustmentEntryRepository
 * Related Entity    : TaxAdjustmentEntry
 * Related DTO       : N/A
 * Related Mapper    : TaxAdjustmentEntryMapper
 * Related DB Table  : tax_adjustment_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxAdjustmentEntryService, TaxAdjustmentEntryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_adjustment_entrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxAdjustmentEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxAdjustmentEntryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_adjustment_entrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_adjustment_entrys}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxAdjustmentEntryRepository extends JpaRepository<TaxAdjustmentEntry, Long> {
    List<TaxAdjustmentEntry> findByCompanyId(Long companyId);
    List<TaxAdjustmentEntry> findByCompanyIdAndStatus(Long companyId, String status);
}