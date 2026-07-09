/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : InternalSettlementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InternalSettlementController
 * Related Service   : InternalSettlementService, InternalSettlementServiceImpl
 * Related Repository: InternalSettlementRepository
 * Related Entity    : InternalSettlement
 * Related DTO       : N/A
 * Related Mapper    : InternalSettlementMapper
 * Related DB Table  : internal_settlements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InternalSettlementService, InternalSettlementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'internal_settlements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.InternalSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code InternalSettlementRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'internal_settlements' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code internal_settlements}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface InternalSettlementRepository extends JpaRepository<InternalSettlement, Long> {
    List<InternalSettlement> findByCompanyId(Long companyId);
}