/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmQuoteRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteController
 * Related Service   : CrmQuoteService, CrmQuoteServiceImpl
 * Related Repository: CrmQuoteRepository
 * Related Entity    : CrmQuote
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteMapper
 * Related DB Table  : crm_quotes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteService, CrmQuoteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_quotes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuoteRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_quotes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_quotes}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmQuoteRepository extends JpaRepository<CrmQuote, Long> {
    Optional<CrmQuote> findByQuoteNumber(String quoteNumber);
    List<CrmQuote> findByCompanyId(Long companyId);
}
