/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : JournalEntryLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryLineController
 * Related Service   : JournalEntryLineService, JournalEntryLineServiceImpl
 * Related Repository: JournalEntryLineRepository
 * Related Entity    : JournalEntryLine
 * Related DTO       : N/A
 * Related Mapper    : JournalEntryLineMapper
 * Related DB Table  : journal_entry_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JournalEntryLineService, JournalEntryLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'journal_entry_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'journal_entry_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code journal_entry_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
    List<JournalEntryLine> findByJournalEntryId(Long journalEntryId);
}
