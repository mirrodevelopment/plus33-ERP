/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmQuoteVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteVersionController
 * Related Service   : CrmQuoteVersionService, CrmQuoteVersionServiceImpl
 * Related Repository: CrmQuoteVersionRepository
 * Related Entity    : CrmQuoteVersion
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteVersionMapper
 * Related DB Table  : crm_quote_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteVersionService, CrmQuoteVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_quote_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmQuoteVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuoteVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_quote_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_quote_versions}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmQuoteVersionRepository extends JpaRepository<CrmQuoteVersion, Long> {
    List<CrmQuoteVersion> findByQuoteId(Long quoteId);
    Optional<CrmQuoteVersion> findByQuoteIdAndVersionNumber(Long quoteId, int versionNumber);
}
