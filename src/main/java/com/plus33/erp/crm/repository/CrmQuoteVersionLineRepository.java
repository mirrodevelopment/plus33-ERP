/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmQuoteVersionLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteVersionLineController
 * Related Service   : CrmQuoteVersionLineService, CrmQuoteVersionLineServiceImpl
 * Related Repository: CrmQuoteVersionLineRepository
 * Related Entity    : CrmQuoteVersionLine
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteVersionLineMapper
 * Related DB Table  : crm_quote_version_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteVersionLineService, CrmQuoteVersionLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_quote_version_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmQuoteVersionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuoteVersionLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_quote_version_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_quote_version_lines}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmQuoteVersionLineRepository extends JpaRepository<CrmQuoteVersionLine, Long> {
    List<CrmQuoteVersionLine> findByQuoteVersionId(Long quoteVersionId);
}
