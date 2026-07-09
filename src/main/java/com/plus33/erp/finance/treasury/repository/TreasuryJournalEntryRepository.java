/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryJournalEntryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryJournalEntryController
 * Related Service   : TreasuryJournalEntryService, TreasuryJournalEntryServiceImpl
 * Related Repository: TreasuryJournalEntryRepository
 * Related Entity    : TreasuryJournalEntry
 * Related DTO       : N/A
 * Related Mapper    : TreasuryJournalEntryMapper
 * Related DB Table  : treasury_journal_entrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryJournalEntryService, TreasuryJournalEntryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_journal_entrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryJournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryJournalEntryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_journal_entrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_journal_entrys}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryJournalEntryRepository extends JpaRepository<TreasuryJournalEntry, Long> {

    Optional<TreasuryJournalEntry> findByReferenceNumber(String referenceNumber);

    List<TreasuryJournalEntry> findByEventTypeAndEventId(String eventType, Long eventId);

    @Query("""
        SELECT e FROM TreasuryJournalEntry e
        WHERE e.company.id = :companyId
          AND e.postingDate BETWEEN :from AND :to
        ORDER BY e.postingDate, e.id
        """)
    List<TreasuryJournalEntry> findByCompanyAndDateRange(
            @Param("companyId") Long companyId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
    @Query("""
        SELECT COALESCE(SUM(CASE WHEN e.entryType = 'DEBIT' THEN e.baseCurrencyAmount ELSE -e.baseCurrencyAmount END), 0)
        FROM TreasuryJournalEntry e
        WHERE e.account.id = :accountId
          AND e.postingDate BETWEEN :from AND :to
        """)
    BigDecimal calculateNetMovement(
            @Param("accountId") Long accountId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}