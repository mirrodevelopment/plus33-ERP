package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByCompanyIdAndEntryNumber(Long companyId, String entryNumber);
    List<JournalEntry> findByCompanyId(Long companyId);

    @Query(value = "SELECT nextval('journal_entry_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
