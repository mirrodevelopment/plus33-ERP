/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : CreditNoteRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteService, CreditNoteServiceImpl
 * Related Repository: CreditNoteRepository
 * Related Entity    : CreditNote
 * Related DTO       : N/A
 * Related Mapper    : CreditNoteMapper
 * Related DB Table  : credit_notes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreditNoteService, CreditNoteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'credit_notes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CreditNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNoteRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'credit_notes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code credit_notes}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long>, JpaSpecificationExecutor<CreditNote> {
    java.util.Optional<CreditNote> findByClientReferenceId(java.util.UUID clientReferenceId);
    Optional<CreditNote> findByCompanyIdAndCreditNoteNumber(Long companyId, String creditNoteNumber);

    @Query(value = "SELECT nextval('credit_note_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}