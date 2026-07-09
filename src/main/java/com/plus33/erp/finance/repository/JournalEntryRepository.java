/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : JournalEntryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryService, JournalEntryServiceImpl
 * Related Repository: JournalEntryRepository
 * Related Entity    : JournalEntry
 * Related DTO       : N/A
 * Related Mapper    : JournalEntryMapper
 * Related DB Table  : journal_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JournalEntryService, JournalEntryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'journal_entrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'journal_entrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code journal_entrys}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByCompanyIdAndEntryNumber(Long companyId, String entryNumber);
    List<JournalEntry> findByCompanyId(Long companyId);

    @Query(value = "SELECT nextval('journal_entry_seq')", nativeQuery = true)
    Long getNextSequenceValue();

    boolean existsByCompanyIdAndSourceModuleAndSourceReference(Long companyId, String sourceModule, String sourceReference);
}
