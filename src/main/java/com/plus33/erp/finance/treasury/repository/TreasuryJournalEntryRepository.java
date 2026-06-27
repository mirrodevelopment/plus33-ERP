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
